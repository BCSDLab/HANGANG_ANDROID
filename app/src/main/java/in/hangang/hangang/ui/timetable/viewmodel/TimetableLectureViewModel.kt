package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
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

    fun getLectures(
            init : Boolean = false,
            classification: List<String>? = null,
            department: String? = null,
            keyword: String? = null,
            semesterDateId: Int
    ) {
        if(init) {
            page = 0
            lectureList.clear()
            _lectures.postValue(lectureList)
        }

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