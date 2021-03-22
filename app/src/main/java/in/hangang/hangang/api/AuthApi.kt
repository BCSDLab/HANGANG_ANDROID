package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.request.TimeTableRequest
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.request.TimeTableCustomLectureRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface AuthApi {
    @GET(AUTH_TEST)
    fun authCheck(): Single<CommonResponse>

    @POST(REFRESH)
    fun refreshToken(): Single<TokenResponse>

    @GET(TIMETABLE)
    fun getTimeTables(
        @Query("semesterDateId") semesterDateId: Long? = null
    ): Single<List<TimeTable>>

    @POST(TIMETABLE)
    fun makeTimeTable(
        @Body userTimeTableRequest: UserTimeTableRequest
    ): Single<CommonResponse>

    @HTTP(method = "DELETE", path = TIMETABLE, hasBody = true)
    fun deleteTimeTable(
        @Body timeTableRequest: TimeTableRequest
    ): Single<CommonResponse>

    @HTTP(method = "PATCH", path = TIMETABLE, hasBody = true)
    fun modifyTimeTableName(
        @Body userTimeTableRequest: UserTimeTableRequest
    ): Single<CommonResponse>

    @POST(TIMETABLE_CUSTOM_LECTURE)
    fun addCustomLectureInTimeTable(
            @Body timeTableCustomLectureRequest: TimeTableCustomLectureRequest
    ): Single<CommonResponse>

    @GET(TIMETABLE_LECTURE)
    fun getLectureListFromTimeTable(
            @Query("timeTableId") timetableId: Int
    ): Single<List<LectureTimeTable>>
}