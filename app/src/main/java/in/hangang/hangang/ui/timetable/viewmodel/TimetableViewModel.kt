package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.TimeTableResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TimetableViewModel(
    private val timetableRepository: TimeTableRepository
) : ViewModelBase() {

    private val lectureList = mutableListOf<Lecture>()

    private val _lectures = MutableLiveData<List<Lecture>>()
    val lectures : LiveData<List<Lecture>> get() = _lectures

    private val _timetables = MutableLiveData<List<TimeTableResponse>>()
    val timetables : LiveData<List<TimeTableResponse>> get() = _timetables

    private val _isGetLecturesLoading = MutableLiveData<Boolean>()
    val isGetLecturesLoading : LiveData<Boolean> get() = _isGetLecturesLoading
    private val _isGetLecturesAdditionalLoading = MutableLiveData<Boolean>()
    val isGetLecturesAdditionalLoading : LiveData<Boolean> get() = _isGetLecturesAdditionalLoading
    private val _getLecturesErrorMessage = MutableLiveData<String>()
    val getLecturesErrorMessage : LiveData<String> get() = _getLecturesErrorMessage

    private var lastLecturesParameter : LecturesParameter = LecturesParameter()

    fun getTimetables() {
        timetableRepository.getTimetables()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _timetables.postValue(it)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }

    fun getLectures(lecturesParameter: LecturesParameter? = null) {
        _isGetLecturesLoading.postValue(true)
        _getLecturesErrorMessage.postValue("")
        lastLecturesParameter = lecturesParameter ?: LecturesParameter()
        lastLecturesParameter.page = 0
        lectureList.clear()
        _lectures.postValue(lectureList)
        getLecturesAdditional()
    }

    fun getLecturesAdditional() {
        _isGetLecturesAdditionalLoading.postValue(true)
        lastLecturesParameter.page += 1
        timetableRepository.getLectures(lastLecturesParameter)
            .handleHttpException()
            .withThread()
            .subscribe({
                _isGetLecturesAdditionalLoading.postValue(false)
                _isGetLecturesLoading.postValue(false)
                lectureList.addAll(it)
                _lectures.postValue(lectureList)
            }, {
                _isGetLecturesAdditionalLoading.postValue(false)
                _isGetLecturesLoading.postValue(false)
                _getLecturesErrorMessage.postValue(it.toCommonResponse().errorMessage)
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }
}