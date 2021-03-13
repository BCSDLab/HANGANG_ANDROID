package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.TimeTableResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import io.reactivex.rxjava3.core.Single

class TimeTableLocalDataSource : TimeTableDataSource {
    override fun getTimetables(): Single<List<TimeTableResponse>> {
        return Single.never()
    }

    override fun getLectures(lecturesParameter: LecturesParameter): Single<List<Lecture>> {
        return Single.never()
    }
}