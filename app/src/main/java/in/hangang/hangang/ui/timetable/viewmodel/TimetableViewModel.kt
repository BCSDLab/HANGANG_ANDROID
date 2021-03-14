package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
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

class TimetableViewModel(
        private val timetableRepository: TimeTableRepository
) : ViewModelBase() {


    private val _timetables = MutableLiveData<List<TimeTable>>()
    private val _mainTimeTable = MutableLiveData<TimeTable>()

    val timetables: LiveData<List<TimeTable>> get() = _timetables
    val mainTimeTable: LiveData<TimeTable> get() = _mainTimeTable

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
            }
        }
    }

    fun setMainTimeTable(timetable: TimeTable) {
        timetableRepository.setMainTimeTable(timetable.id).subscribe({
            findTimeTableById(timetable.id).withThread().subscribe({
                _mainTimeTable.postValue(it)
            }, {

            })
        }, {
            LogUtil.e(it.message)
        })
    }

    fun findTimeTableById(timetableId: Int): Single<TimeTable> {
        return Single.create { subscriber ->
            if (_timetables.value == null || _timetables.value!!.isEmpty())
                subscriber.onError(Exception("Please call getTimetables before calling this"))

            with(_timetables.value) {
                val timetable = this?.find { it.id == timetableId }

                if (timetable == null)
                    subscriber.onError(Exception("No matches timetableId in timetable list"))

                subscriber.onSuccess(timetable!!)
            }
        }
    }
}