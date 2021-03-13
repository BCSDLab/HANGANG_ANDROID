package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.request.LecturesParameter
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
    private val _timetables = MutableLiveData<List<TimeTable>>()
    private val _currentShowingTimeTable = MutableLiveData<TimeTable>()

    private val _isGetLecturesLoading = MutableLiveData<Boolean>()
    private val _isGetLecturesAdditionalLoading = MutableLiveData<Boolean>()

    private val _getLecturesErrorMessage = MutableLiveData<String>()

    val isGetLecturesAdditionalLoading: LiveData<Boolean> get() = _isGetLecturesAdditionalLoading
    val lectures: LiveData<List<Lecture>> get() = _lectures
    val timetables: LiveData<List<TimeTable>> get() = _timetables
    val currentShowingTimeTable: LiveData<TimeTable> get() = _currentShowingTimeTable
    val isGetLecturesLoading: LiveData<Boolean> get() = _isGetLecturesLoading
    val getLecturesErrorMessage: LiveData<String> get() = _getLecturesErrorMessage

    private var lastLecturesParameter: LecturesParameter = LecturesParameter()

    fun getTimetables() {
        timetableRepository.getTimeTables()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({ list ->
                _timetables.postValue(list)
                timetableRepository.getMainTimeTable().subscribe({ timetableId ->
                    _currentShowingTimeTable.postValue(
                        list.find { it.id == timetableId } ?: list[0].also {
                            timetableRepository.setMainTimeTable(it.id)
                        })
                }, {
                    _currentShowingTimeTable.postValue(list[0].also {
                        timetableRepository.setMainTimeTable(it.id)
                    })
                })
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

    private fun getLecturesAdditional() {
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