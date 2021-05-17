package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.data.evaluation.LectureTimeTable
import `in`.hangang.hangang.data.evaluation.TimeTable
import `in`.hangang.hangang.data.evaluation.TimeTableWithLecture
import `in`.hangang.hangang.data.evaluation.TimetableMemo
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.TimeTableDataSource
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.core.Single

class TimeTableLocalDataSource : TimeTableDataSource {
    override fun getTimeTables(): Single<Map<Int, List<TimeTable>>> {
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



    override fun getTimetable(timetableId: Int): Single<TimeTableWithLecture> {
        return Single.never()
    }

    override fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun removeLectureFromTimeTable(
        lectureId: Int,
        timetableId: Int
    ): Single<CommonResponse> {
        return Single.never()
    }





}