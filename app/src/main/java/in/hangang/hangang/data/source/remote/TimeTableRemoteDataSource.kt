package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.data.request.TimeTableRequest
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import io.reactivex.rxjava3.core.Single

class TimeTableRemoteDataSource(
    private val noAuthApi: NoAuthApi,
    private val authApi: AuthApi,
    private val refreshApi: AuthApi
) : TimeTableDataSource {
    override fun getTimeTables(): Single<List<TimeTable>> {
        return Single.create { subscriber ->
            authApi.getTimeTables().subscribe({ list1 ->
                if(list1.isEmpty()) {
                    makeTimeTable(
                        UserTimeTableRequest(
                            name = "2021년 1학기 (1)",
                            semesterDateId = 1
                        )
                    ).subscribe({
                        authApi.getTimeTables().subscribe({ list2 ->
                            subscriber.onSuccess(list2)
                        }, {
                            subscriber.onError(it)
                        })
                    }, {
                        subscriber.onError(it)
                    })
                } else
                    subscriber.onSuccess(list1)
            }, {
                subscriber.onError(it)
            })
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

    override fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse> {
        return authApi.makeTimeTable(userTimeTableRequest)
    }

    override fun removeTimeTable(timetableId: Int): Single<CommonResponse> {
        return authApi.deleteTimeTable(TimeTableRequest(userTimeTableId = timetableId))
    }

    override fun modifyTimeTableName(timetableId: Int, name: String): Single<CommonResponse> {
        return authApi.modifyTimeTableName(
            UserTimeTableRequest(
                id = timetableId,
                name = name
            )
        )
    }

    override fun setMainTimeTable(timetableId: Int): Single<Int> {
        return Single.never()
    }

    override fun getMainTimeTable(): Single<Int> {
        return Single.never()
    }
}