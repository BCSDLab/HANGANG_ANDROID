package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.evaluation.LectureTimeTable
import `in`.hangang.hangang.data.evaluation.TimeTable
import `in`.hangang.hangang.data.evaluation.TimeTableWithLecture
import `in`.hangang.hangang.data.evaluation.TimetableMemo
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import io.reactivex.rxjava3.core.Single

class TimeTableRepository(
    private val timeTableLocalDataSource: TimeTableDataSource,
    private val timeTableRemoteDataSource: TimeTableDataSource
) : TimeTableDataSource {
    override fun getTimeTables(): Single<Map<Int, List<TimeTable>>> {
        return timeTableRemoteDataSource.getTimeTables()
    }


    override fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse> {
        return timeTableRemoteDataSource.makeTimeTable(userTimeTableRequest)
    }

    override fun removeTimeTable(timetableId: Int): Single<CommonResponse> {
        return timeTableRemoteDataSource.removeTimeTable(timetableId)
    }

    override fun modifyTimeTableName(
        timetableId: Int,
        name: String
    ): Single<CommonResponse> {
        return timeTableRemoteDataSource.modifyTimeTableName(timetableId, name)
    }


    override fun getTimetable(timetableId: Int): Single<TimeTableWithLecture> {
        return timeTableRemoteDataSource.getTimetable(timetableId)
    }

    override fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse> {
        return timeTableRemoteDataSource.addLectureInTimeTable(lectureId, timetableId)
    }

    override fun removeLectureFromTimeTable(
        lectureId: Int,
        timetableId: Int
    ): Single<CommonResponse> {
        return timeTableRemoteDataSource.removeLectureFromTimeTable(lectureId, timetableId)
    }







}