package `in`.hangang.hangang.ui.timetable.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.entity.timetable.LectureTimeTable
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.LectureRepository
import `in`.hangang.hangang.data.source.repository.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class TimetableLectureDetailViewModel(
        private val timeTableRepository: TimeTableRepository,
        private val lectureRepository: LectureRepository
) : ViewModelBase() {

    private val _lectureTimetable = MutableLiveData<LectureTimeTable>()//이거사용
    private val _memo = MutableLiveData<Event<String>>()
    private val _lecture = MutableLiveData<RankingLectureItem>()

    val lectureTimetable: LiveData<LectureTimeTable> get() = _lectureTimetable
    val memo: LiveData<Event<String>> get() = _memo
    val lecture: LiveData<RankingLectureItem> get() = _lecture

    private fun setLectureTimetable(lectureTimeTable: LectureTimeTable) {
        _lectureTimetable.value = lectureTimeTable
    }

    fun initWithLectureTimetable(lectureTimeTable: LectureTimeTable) {
        setLectureTimetable(lectureTimeTable)
        getMemo(lectureTimeTable)
    }

    fun getMemo(lectureTimeTable: LectureTimeTable) {
        timeTableRepository.getMemo(lectureTimeTable.id)
                .withThread()
                .handleHttpException()
                .handleProgress(this)
                .subscribe({
                    _memo.postValue(Event(it.memo ?: ""))
                }, {
                    _memo.postValue(Event(""))
                })
                .addTo(compositeDisposable)
    }

    fun updateMemo(memo: String) {
        _lectureTimetable.value?.id?.let { id ->
            (if (memo.isBlank()) {
                timeTableRepository.removeMemo(id)
            } else {
                timeTableRepository.modifyMemo(
                        timetableLectureId = id,
                        memo = memo
                )
            })
                    .withThread()
                    .handleHttpException()
                    .handleProgress(this)
                    .subscribe({
                        getMemo(_lectureTimetable.value!!)
                    }, {

                    })
                    .addTo(compositeDisposable)
        }
    }
    fun getLectureId(lectureId: Int){
        lectureRepository.getLecturesId(lectureId)
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _lecture.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })

    }

}