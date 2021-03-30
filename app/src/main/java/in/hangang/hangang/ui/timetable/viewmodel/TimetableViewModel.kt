package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.core.util.init
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo

class TimetableViewModel(
    private val timetableRepository: TimeTableRepository
) : ViewModelBase() {

    private val timetableList = mutableListOf<TimeTable>()

    private val _timetables = MutableLiveData<List<TimeTable>>()
    private val _mainTimeTable = MutableLiveData<TimeTable>()
    private val _setMainTimeTable = MutableLiveData<Event<TimeTable>>()
    private val _timetableNameModified = MutableLiveData<Event<String>>()

    val timetables: LiveData<List<TimeTable>> get() = _timetables
    val mainTimeTable: LiveData<TimeTable> get() = _mainTimeTable
    val setMainTimeTable: LiveData<Event<TimeTable>> get() = _setMainTimeTable
    val timetableNameModified: LiveData<Event<String>> get() = _timetableNameModified

    /*fun getTimetables() {
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
    }*/

    fun getTimetables() {
        Single.zip(
            timetableRepository.getTimeTables(),
            timetableRepository.getMainTimeTable()
        ) { timetables, mainTimetableId ->
            timetableList.init(timetables)
            _timetables.postValue(timetables)
            findTimeTableById(mainTimetableId)?.let {
                _mainTimeTable.postValue(it)
                it
            }
        }.flatMap { timetable ->
            if (timetable == null) {
                timetableRepository.setMainTimeTable(timetableList[0].id)
                    .flatMap {
                        Single.just(timetableList[0])
                    }
            } else
                Single.just(timetable)
        }
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _mainTimeTable.postValue(it)
            }, {
                Logger.e(it.toCommonResponse().errorMessage ?: "Unknown error")
            })
    }

    fun getMainTimeTable() {
        timetableRepository.getMainTimeTable()
            .flatMap { //시간표 id를 가지고 시간표 객체를 얻어옴
                if (timetableList.isEmpty())
                    Single.error(NoSuchElementException("You must call getTimetables before call getMainTimeTable"))
                else {
                    findTimeTableById(it)?.let { timetable ->
                        Single.just(timetable)
                    }
                }
            }
            .flatMap { //메인 시간표가 설정이 안되어 있을 경우 리스트의 0번 아이템을 메인 시간표로 설정
                if (it == null) {
                    timetableRepository.setMainTimeTable(timetableList[0].id)
                        .flatMap {
                            Single.just(timetableList[0])
                        }
                } else
                    Single.just(it)
            }
            .subscribe({
                _mainTimeTable.postValue(it)
            }, {
                //TODO 메인 시간표를 가져오는 중 오류
            })
    }

    fun setMainTimeTable(timetable: TimeTable) {
        timetableRepository.setMainTimeTable(timetable.id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _setMainTimeTable.postValue(Event(timetable))
            }, {
                //TODO 메인 시간표 설정 중 오류
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

    private fun findTimeTableById(timetableId: Int): TimeTable? {
        return timetableList.find { it.id == timetableId }
    }
}