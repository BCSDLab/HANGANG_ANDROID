package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.TimeTableResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import io.reactivex.rxjava3.core.Single

class TimeTableRemoteDataSource(
    private val noAuthApi: NoAuthApi,
    private val authApi: AuthApi,
    private val refreshApi: AuthApi
) : TimeTableDataSource {
    override fun getTimetables(): Single<List<TimeTableResponse>> {
        return authApi.getTimetables().doOnSuccess {
            if (it.isEmpty()) {
                //TODO make default timetable
            }
        }
    }

    override fun getLectures(lecturesParameter: LecturesParameter): Single<List<Lecture>> {
        return noAuthApi.getLectures(
            lecturesParameter.classification,
            lecturesParameter.department,
            lecturesParameter.hashTag,
            lecturesParameter.keyword,
            lecturesParameter.limit,
            lecturesParameter.page,
            lecturesParameter.sort
        )
    }
}