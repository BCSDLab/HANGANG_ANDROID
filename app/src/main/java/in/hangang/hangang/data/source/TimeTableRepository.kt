package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.TimeTableResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import io.reactivex.rxjava3.core.Single

class TimeTableRepository(
    private val timeTableLocalDataSource: TimeTableDataSource,
    private val timeTableRemoteDataSource: TimeTableDataSource
) : TimeTableDataSource {
    override fun getTimetables(): Single<List<TimeTableResponse>> {
        return timeTableRemoteDataSource.getTimetables()
    }

    override fun getLectures(lecturesParameter: LecturesParameter): Single<List<Lecture>> {
        return timeTableRemoteDataSource.getLectures(lecturesParameter)
    }
}