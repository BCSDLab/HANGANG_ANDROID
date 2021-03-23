package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.withThread
import androidx.compose.runtime.key
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TimetableLectureViewModel(
    private val timetableRepository: TimeTableRepository
) : ViewModelBase() {
    private val lectureList = mutableListOf<LectureTimeTable>()

    private val _lectures = MutableLiveData<List<LectureTimeTable>>()

    private val _isGetLecturesLoading = MutableLiveData<Boolean>()
    private val _isGetLecturesAdditionalLoading = MutableLiveData<Boolean>()

    val lectures: LiveData<List<LectureTimeTable>> get() = _lectures
    val isGetLecturesLoading: LiveData<Boolean> get() = _isGetLecturesLoading

    private var lastLecturesParameter: LecturesParameter = LecturesParameter()

    var page = 0
    var classification: List<String>? = null
    var department: String? = null
    var keyword: String? = null
    var semesterDateId: Int = 5

    fun getLectures(
        classification: List<String>? = null,
        department: String? = null,
        keyword: String? = null,
        semesterDateId: Int
    ) {
        page = 0
        this.classification = classification
        this.department = department
        this.keyword = keyword
        this.semesterDateId = semesterDateId
        lectureList.clear()
        _lectures.postValue(lectureList)

        getLectures()
    }

    fun getLectures() {
        page++
        timetableRepository.getLectureTimetableList(
            classification = classification,
            department = department,
            keyword = keyword,
            page = page,
            semesterDateId = semesterDateId
        )
            .handleHttpException()
            .withThread()
            .doFinally {
                _isGetLecturesAdditionalLoading.postValue(false)
                _isGetLecturesLoading.postValue(false)
            }
            .subscribe({
                lectureList.addAll(it)
                _lectures.postValue(lectureList)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }
}