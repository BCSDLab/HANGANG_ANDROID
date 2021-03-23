package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.constant.MAIN_TIMETABLE
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.core.Single

class TimeTableLocalDataSource : TimeTableDataSource {
    override fun getTimeTables(): Single<List<TimeTable>> {
        return Single.never()
    }

    override fun getLectureTimetableList(classification: List<String>?, department: String?, keyword: String?, limit: Int, page: Int, semesterDateId: Int): Single<List<LectureTimeTable>> {
        return Single.never()
    }

    override fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse> {
        return Single.never()
    }

    override fun removeTimeTable(timetableId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun modifyTimeTableName(timetableId: Int, name: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun setMainTimeTable(timetableId: Int): Single<Int> {
        return Single.create { subscriber ->
            try {
                Hawk.put(MAIN_TIMETABLE, timetableId)
                subscriber.onSuccess(timetableId)
            } catch (t: Throwable) {
                subscriber.onError(t)
            }
        }
        //return Single.never()
    }

    override fun getMainTimeTable(): Single<Int> {
        return Single.create { subscriber ->
            try {
                subscriber.onSuccess(Hawk.get(MAIN_TIMETABLE))
            } catch (t: Throwable) {
                subscriber.onError(t)
            }
        }
        //return Single.never()
    }

    override fun getLectureList(timetableId: Int): Single<List<LectureTimeTable>> {
        return Single.never()
    }

    override fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun removeLectureInTimeTable(
        lectureId: Int,
        timetableId: Int
    ): Single<CommonResponse> {
        return Single.never()
    }
}