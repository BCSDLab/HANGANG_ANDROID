package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo

class TimetableViewModel(
        private val timetableRepository: TimeTableRepository
) : ViewModelBase() {

    private val _timetables = MutableLiveData<List<TimeTable>>()
    private val _mainTimeTable = MutableLiveData<TimeTable>()
    private val _setMainTimeTable = MutableLiveData<Event<TimeTable>>()
    private val _timetableNameModified = MutableLiveData<Event<String>>()

    val timetables: LiveData<List<TimeTable>> get() = _timetables
    val mainTimeTable: LiveData<TimeTable> get() = _mainTimeTable
    val setMainTimeTable: LiveData<Event<TimeTable>> get() = _setMainTimeTable
    val timetableNameModified: LiveData<Event<String>> get() = _timetableNameModified

    fun getTimetables() {
        timetableRepository.getTimeTables()
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({ list ->
                    _timetables.value = list
                    getMainTimeTable()
                }, {
                    LogUtil.e(it.toCommonResponse().errorMessage)
                })
                .addTo(compositeDisposable)
    }

    fun getMainTimeTable() {
        if (_timetables.value == null || _timetables.value!!.isEmpty()) {
            LogUtil.e("Please call getTimetables before calling this")
        } else {
            with(_timetables.value!!) {
                timetableRepository.getMainTimeTable().subscribe({ timetableId ->
                    findTimeTableById(timetableId).withThread().subscribe({
                        _mainTimeTable.postValue(it)
                    }, {
                        setMainTimeTable(this[0])
                    })
                }, {
                    setMainTimeTable(this[0])
                })
                    .addTo(compositeDisposable)
            }
        }
    }

    fun setMainTimeTable(timetable: TimeTable) {
        timetableRepository.setMainTimeTable(timetable.id).subscribe({
            findTimeTableById(timetable.id)
                    .withThread()
                    .handleProgress(this)
                    .subscribe({
                        _setMainTimeTable.postValue(Event(it))
                    }, {

                    })
        }, {
            LogUtil.e(it.message)
        })
                .addTo(compositeDisposable)
    }

    fun modifyTimeTableName(timetable: TimeTable, name: String) {
        timetableRepository.modifyTimeTableName(timetable.id, name)
                .withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    _timetableNameModified.postValue(Event(name))
                }, {

                })
                .addTo(compositeDisposable)
    }

    fun removeTimetable(timetable: TimeTable) {
        timetableRepository.removeTimeTable(timetableId = timetable.id)
                .withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    getTimetables()
                }, {

                })
                .addTo(compositeDisposable)
    }

    fun findTimeTableById(timetableId: Int): Single<TimeTable> {
        return Single.create { subscriber ->
            if (_timetables.value == null || _timetables.value!!.isEmpty())
                subscriber.onError(Exception("Please call getTimetables before calling this"))
            else with(_timetables.value) {
                val timetable = this?.find { it.id == timetableId }

                if (timetable == null)
                    subscriber.onError(Exception("No matches timetableId in timetable list"))
                else subscriber.onSuccess(timetable)
            }
        }
    }
}