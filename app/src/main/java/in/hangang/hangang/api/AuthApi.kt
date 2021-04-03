package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.MainTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.entity.TimetableMemo
import `in`.hangang.hangang.data.request.TimeTableCustomLectureRequest
import `in`.hangang.hangang.data.request.TimeTableRequest
import `in`.hangang.hangang.data.request.TimetableMemoRequest
import `in`.hangang.hangang.data.request.UserTimeTableRequest
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

    @POST(TIMETABLE_LECTURE)
    fun addLectureInTimeTable(
            @Body timeTableRequest: TimeTableRequest
    ): Single<CommonResponse>

    @HTTP(method = "DELETE", path = TIMETABLE_LECTURE, hasBody = true)
    fun removeLectureInTimeTable(
            @Body timeTableRequest: TimeTableRequest
    ): Single<CommonResponse>

    @GET(TIMETABLE_LECTURE)
    fun getLectureListFromTimeTable(
            @Query("timeTableId") timetableId: Int
    ): Single<List<LectureTimeTable>>

    @POST(TIMETABLE_CUSTOM_LECTURE)
    fun addCustomLectureInTimetable(
            @Body timeTableCustomLectureRequest: TimeTableCustomLectureRequest
    ): Single<CommonResponse>

    @GET(TIMETABLE_LECTURE_LIST)
    fun getTimetableLectureList(
            @Query("classification") classification: List<String>? = null,
            @Query("department") department: String? = null,
            @Query("keyword") keyword: String? = null,
            @Query("limit") limit: Int = 10,
            @Query("page") page: Int = 1,
            @Query("semesterDateId") semesterDateId: Int
    ): Single<List<LectureTimeTable>>

    @GET(TIMETABLE_MAIN)
    fun getMainTimeTable(): Single<MainTimeTable>

    @HTTP(method = "PATCH", path = TIMETABLE_MAIN, hasBody = true)
    fun setMainTimeTable(
            @Body timeTableRequest: TimeTableRequest
    ): Single<CommonResponse>

    @GET(TIMETABLE_MEMO)
    fun getTimetableMemo(
            @Query("timeTableId") timetableId: Int
    ): Single<TimetableMemo>

    @POST(TIMETABLE_MEMO)
    fun addTimetableMemo(
            @Body timetableMemoRequest: TimetableMemoRequest
    ): Single<CommonResponse>

    @HTTP(method = "PATCH", path = TIMETABLE_MEMO, hasBody = true)
    fun modifyTimetableMemo(
            @Body timetableMemoRequest: TimetableMemoRequest
    ): Single<CommonResponse>

    @HTTP(method = "DELETE", path = TIMETABLE_MEMO, hasBody = true)
    fun removeTimetableMemo(
            @Body timetableMemoRequest: TimetableMemoRequest
    ): Single<CommonResponse>

    @GET(TIMETABLE_SCRAP)
    fun getScrapLectures(): Single<List<LectureTimeTable>>

    @POST(TIMETABLE_SCRAP)
    fun scrapLecture(
            @Body timeTableRequest: TimeTableRequest
    ): Single<CommonResponse>

    @HTTP(method = "DELETE", path = TIMETABLE_SCRAP, hasBody = true)
    fun unscrapLecture(
            @Body timeTableRequest: TimeTableRequest
    ): Single<CommonResponse>
}