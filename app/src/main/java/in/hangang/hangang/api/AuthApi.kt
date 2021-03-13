package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.AUTH_TEST
import `in`.hangang.hangang.constant.REFRESH
import `in`.hangang.hangang.constant.TIMETABLE
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TimeTableResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @GET(AUTH_TEST)
    fun authCheck(): Single<CommonResponse>

    @POST(REFRESH)
    fun refreshToken(): Single<TokenResponse>

    @GET(TIMETABLE)
    fun getTimetables(): Single<List<TimeTableResponse>>

    @GET(TIMETABLE)
    fun getTimetables(
        @Query("semesterDateId") semesterDateId: Long
    ): Single<List<TimeTableResponse>>
}