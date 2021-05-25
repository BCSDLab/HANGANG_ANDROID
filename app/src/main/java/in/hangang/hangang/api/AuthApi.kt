package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.evaluation.*
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.request.TimeTableRequest
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

    @GET(LECTURE_SCRAPED)
    fun getScrapedLecture(): Single<ArrayList<RankingLectureItem>>

    @GET(EVALUATION_RATING)
    fun getEvaluationRating(@Path("id")id: Int): Single<ArrayList<Int>>

    @GET(EVALUTATION_TOTAL)
    fun getEvalutaionTotal(@Path("id")id: Int): Single<Evaluation>

    @GET(CLASS_LECTURES)
    fun getClassLectures(@Path("id") id: Int): Single<ArrayList<ClassLecture>>

    @GET(LECTURE_REVIEWS)
    fun getLectureReview(@Path("id") id: Int,
                         @Query("limit") limit: Int = 20,
                        @Query("page") page: Int,
                         @Query("keyword") keyword: String?,
                        @Query("sort") sort: String): Single<LectureReviewResult>

    @GET(LECTURE_DOCUMENTS)
    fun getRecommentedDocs(@Query("category") category: ArrayList<String>? = null,
                           @Query("department") department: ArrayList<String>? = null,
                           @Query("keyword") keyword: String? = null,
                           @Query("limit") limit: Int = 20,
                           @Query("order") order: String = "hits",
                           @Query("page") page: Int = 1): Single<LectureDocResult>
    @POST(REVIEW_RECOMMEND)
    fun postReviewRecommend(
        @Body reviewRecommendRequest: ReviewRecommendRequest
    ): Single<CommonResponse>

    @GET(LECTURE_REVIEW)
    fun getLectureReviewItem(@Path("id") id: Int): Single<LectureReview>

    @GET(LECTURE_SEMESTER)
    fun getLectureSemester(@Path("id") id: Int): Single<ArrayList<String>>

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
    ): Single<TimeTableWithLecture>

}


