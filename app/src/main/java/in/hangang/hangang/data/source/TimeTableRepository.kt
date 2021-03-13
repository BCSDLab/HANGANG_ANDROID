package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import io.reactivex.rxjava3.core.Single

class TimeTableRepository(
    private val timeTableLocalDataSource: TimeTableDataSource,
    private val timeTableRemoteDataSource: TimeTableDataSource
) : TimeTableDataSource {
    override fun getTimeTables(): Single<List<TimeTable>> {
        return timeTableRemoteDataSource.getTimeTables()
    }

    override fun getLectures(lecturesParameter: LecturesParameter): Single<List<Lecture>> {
        return timeTableRemoteDataSource.getLectures(lecturesParameter)
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

    override fun setMainTimeTable(timetableId: Int): Single<Int> {
        return timeTableLocalDataSource.setMainTimeTable(timetableId)
    }

    override fun getMainTimeTable(): Single<Int> {
        return timeTableLocalDataSource.getMainTimeTable()
    }
}