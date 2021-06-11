package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.evaluation.*
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTableWithLecture
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.entity.TimetableMemo
import `in`.hangang.hangang.data.request.TimeTableCustomLectureRequest
import `in`.hangang.hangang.data.request.TimeTableRequest
import `in`.hangang.hangang.data.request.TimetableMemoRequest
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.user.User
import `in`.hangang.hangang.data.request.LectureBankReportRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.LectureBankCommentResponse
import `in`.hangang.hangang.data.response.LectureBankResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Call
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
    ): Single<TimeTableWithLecture>

    @POST(TIMETABLE_CUSTOM_LECTURE)
    fun addCustomLectureInTimetable(
            @Body timeTableCustomLectureRequest: TimeTableCustomLectureRequest
    ): Single<CommonResponse>

    @GET(TIMETABLE_LECTURE_LIST)
    fun getTimetableLectureList(
            @Query("classification") classification: List<String>? = null,
            @Query("criteria") criteria: String? = null,
            @Query("department") department: String? = null,
            @Query("keyword") keyword: String? = null,
            @Query("limit") limit: Int = API_TIMETABLE_DEFAULT_LIMIT,
            @Query("page") page: Int = API_TIMETABLE_DEFAULT_PAGE,
            @Query("semesterDateId") semesterDateId: Int
    ): Single<List<LectureTimeTable>>

    @GET(TIMETABLE_MAIN)
    fun getMainTimeTable(): Single<TimeTableWithLecture>

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

    @GET(LECTURE_BANKS)
    fun getLectureBanks(
        @Query("category") categories: List<String>?,
    @Query("department") department: String?,
    @Query("keyword") keyword: String?,
    @Query("limit") limit: Int,
    @Query("order") order: String,
    @Query("page") page : Int) : Single<LectureBankResponse>

    @GET("$LECTURE_BANKS/{id}")
    fun getLectureBankDetail(@Path("id") id: Int) : Single<LectureBankDetail>

    @GET(LECTURE_BANKS_HIT)
    fun hitLectureBank(@Path("id") id: Int) : Single<CommonResponse>

    @POST(LECTURE_BANKS_PURCHASE)
    fun purchaseLectureBank(@Path("id") id: Int) : Single<CommonResponse>

    @GET(LECTURE_BANKS_PURCHASE_CHECK)
    fun checkLectureBankPurchased(@Path("id") id: Int) : Single<Boolean>

    @POST("$LECTURE_BANKS_SCRAP/{id}")
    fun scrapLectureBank(@Path("id") id: Int) : Single<Int>

    @HTTP(method = "DELETE", path = LECTURE_BANKS_SCRAP, hasBody = true)
    fun unscrapLectureBanks(@Body ids: List<Int>) : Single<CommonResponse>

    @POST(LECTURE_BANKS_REPORT)
    fun reportLectureBank(@Body lectureBankReportRequest: LectureBankReportRequest) : Single<CommonResponse>

    @GET(LECTURE_BANKS_COMMENTS)
    fun getLectureBankComments(
        @Path("id") id: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ) : Single<LectureBankCommentResponse>

    @POST(LECTURE_BANKS_COMMENT)
    fun commentLectureBank(
        @Path("id") id: Int,
        @Body lectureBankComment: LectureBankComment
    ) : Single<Int>

    @POST(LECTURE_BANKS_REPORT_COMMENT)
    fun reportLectureBankComment(@Body lectureBankReportRequest: LectureBankReportRequest) : Single<CommonResponse>

    @GET(LECTURE_BANKS_FILE)
    fun downloadSingleFile(@Path("id") id: Int) : Single<String>

    @PUT(LECTURE_BANKS_COMMENT_WITH_ID)
    fun modifyLectureBankComment(@Path("id") lectureBankId: Int, @Path("commentId") commentId: Int, @Body lectureBankComment: LectureBankComment) : Single<CommonResponse>

    @DELETE(LECTURE_BANKS_COMMENT_WITH_ID)
    fun deleteLectureBankComment(@Path("id") lectureBankId: Int, @Path("commentId") commentId: Int) : Single<CommonResponse>

    @Multipart
    @POST(LECTURE_BANKS_FILES)
    fun uploadFile(@Part file : MultipartBody.Part) : Call<List<String>>

    @GET(USER_ME)
    fun getUserInfo() : Single<User>
}
