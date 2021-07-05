package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.evaluation.*
import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankScrap
import `in`.hangang.hangang.data.entity.mypage.PointRecord
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankPostRequest
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.entity.ranking.RankingLectureResult
import `in`.hangang.hangang.data.entity.timetable.LectureTimeTable
import `in`.hangang.hangang.data.entity.timetable.TimeTable
import `in`.hangang.hangang.data.entity.timetable.TimeTableWithLecture
import `in`.hangang.hangang.data.entity.timetable.TimetableMemo
import `in`.hangang.hangang.data.entity.user.User
import `in`.hangang.hangang.data.entity.user.UserCount
import `in`.hangang.hangang.data.request.*
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.MyProfileResponse
import `in`.hangang.hangang.data.response.TimetableListResponse
import `in`.hangang.hangang.data.response.TokenResponse
import `in`.hangang.hangang.data.response.*
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
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

    @GET(TIMETABLE)
    suspend fun fetchTimeTables(
        @Query("semesterDateId") semesterDateId: Long? = null
    ): List<TimeTable>

    @POST(TIMETABLE)
    fun makeTimeTable(
            @Body userTimeTableRequest: UserTimeTableRequest
    ): Single<String>

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
    ): Single<LectureTimeTable>

    @HTTP(method = "DELETE", path = TIMETABLE_LECTURE, hasBody = true)
    fun removeLectureInTimeTable(
            @Body timeTableRequest: TimeTableRequest
    ): Single<CommonResponse>

    @GET(TIMETABLE_LECTURE)
    fun getLectureListFromTimeTable(
        @Query("timeTableId") timetableId: Int
    ): Single<TimeTableWithLecture>

    @GET(MyProfile)
    fun setMyProfile(): Single<MyProfileResponse>

    @PUT(SaveMyProfile)
    fun saveMyProfile(
        @Body saveMyProfileRequest: SaveMyProfileRequest
    ): Single<CommonResponse>

    @HTTP(method = "DELETE", path = Delete_Account, hasBody = true)
    fun deleteAccount(): Single<CommonResponse>

    @PUT(Logout_All)
    fun logoutAll() : Single<CommonResponse>

    @GET(TIMETABLE_LECTURE)
    suspend fun fetchLectureListFromTimeTable(
        @Query("timeTableId") timetableId: Int
    ): TimeTableWithLecture


    @POST(TIMETABLE_CUSTOM_LECTURE)
    fun addCustomLectureInTimetable(
            @Body timeTableCustomLectureRequest: TimeTableCustomLectureRequest
    ): Single<LectureTimeTable>

    @GET(TIMETABLE_LECTURE_LIST)
    fun getTimetableLectureList(
            @Query("classification") classification: List<String>? = null,
            @Query("criteria") criteria: String? = null,
            @Query("department") department: String? = null,
            @Query("keyword") keyword: String? = null,
            @Query("limit") limit: Int = API_TIMETABLE_DEFAULT_LIMIT,
            @Query("page") page: Int = API_TIMETABLE_DEFAULT_PAGE,
            @Query("semesterDateId") semesterDateId: Int
    ): Single<TimetableListResponse>

    @GET(TIMETABLE_MAIN)
    fun getMainTimeTable(): Single<TimeTableWithLecture>

    @HTTP(method = "PATCH", path = TIMETABLE_MAIN, hasBody = true)
    fun setMainTimeTable(
            @Body timeTableRequest: TimeTableRequest
    ): Single<CommonResponse>

    @GET(TIMETABLE_MEMO)
    fun getTimetableMemo(
            @Query("timetableComponentId") timetableId: Int
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

    @POST(LECTURE_SCRAPED)
    fun postScrapedLecture(@Body scrapedLecture: LectureEvaluationIdRequest): Single<CommonResponse>

    @HTTP(method = "DELETE", path = LECTURE_SCRAPED, hasBody = true)
    fun deleteScrapedLecture(@Body scrapedLecture: ArrayList<Int>): Single<CommonResponse>

    @GET(EVALUATION_RATING)
    fun getEvaluationRating(@Path("id")id: Int): Single<ArrayList<Int>>

    @GET(EVALUTATION_TOTAL)
    fun getEvalutaionTotal(@Path("id")id: Int): Single<Evaluation>

    @GET(CLASS_LECTURES)
    fun getClassLectures(@Path("id") id: Int): Single<ArrayList<ClassLecture>>

    @GET(CLASS_LECTURES)
    suspend fun fetchClassLectures(@Path("id") id: Int): List<ClassLecture>

    @GET(CLASS_LECTURES)
    suspend fun getLectureClass(@Path("id") id: Int): ArrayList<ClassLecture>

    @GET(LECTURES_ID)
    fun getLecturesId(@Path("id") id: Int): Single<RankingLectureItem>


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
    fun getLectureSemester(@Path("id") id: Int): Single<ArrayList<Int>>

    @POST(REVIEW_REPORT)
    fun reportLectureReview(@Body lectureReviewReportRequest: LectureReviewReportRequest): Single<CommonResponse>

    @POST(EVALUATIONS)
    fun postEvaluation(@Body lectureEvaluationRequest: LectureEvaluationRequest): Single<CommonResponse>

    @GET(LECTURES_RANKING)
    fun getLectureRanking(
        @Query("classification") classification: ArrayList<String>? = null,
        @Query("department") department: ArrayList<String>? = null,
        @Query("hash_tag") hashTag: ArrayList<Int>? = null,
        @Query("keyword") keyword: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int? = null,
        @Query("sort") sort: String? = null
    ): Single<RankingLectureResult>

    @GET(LECTURE_BANK_HIT)
    fun getLectureBankHit(): Single<List<LectureDoc>>

    @GET(USER_ME)
    fun getUserInformation(): Single<User>

    @GET(USER_LECTURE)
    fun getUserCounts(): Single<UserCount>

    @GET(USER_POINT_RECORD)
    fun getUserPointRecord(): Single<List<PointRecord>>

    @GET(USER_PURCHASED)
    fun getUserPurchasedBanks(): Single<List<LectureBank>>

    @POST(LECTURE_SCRAPED)
    fun addScrapLecture(@Body scrapLectureRequest: ScrapLectureRequest): Single<CommonResponse>

    @HTTP(method = "DELETE", path = LECTURE_SCRAPED, hasBody = true)
    fun removeScrapLecture(@Body lectureIds: List<Int>): Single<CommonResponse>

    @GET(LECTURE_BANK_SCRAP)
    fun getLectureBankScraps(): Single<List<LectureBankScrap>>

    @HTTP(method = "DELETE", path = LECTURE_BANK_SCRAP, hasBody = true)
    fun unscrapLectureBank(@Body scrapIds: List<Int>): Single<CommonResponse>

    @GET(LECTURE_BANKS)
    fun getLectureBanks(
        @Query("category") categories: List<String>?,
    @Query("department") department: String?,
    @Query("keyword") keyword: String?,
    @Query("limit") limit: Int,
    @Query("order") order: String,
    @Query("page") page : Int) : Single<LectureBankResponse>

    @POST(LECTURE_BANKS)
    fun createLectureBank(@Body lectureBankPostRequest: LectureBankPostRequest) : Single<CommonResponse>

    @GET("$LECTURE_BANKS/{id}")
    fun getLectureBankDetail(@Path("id") id: Int) : Single<LectureBankDetail>

    @POST("$LECTURE_BANKS_HIT/{id}")
    fun toggleHitLectureBank(@Path("id") id: Int) : Single<CommonResponse>

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

    @POST(LECTURE_BANKS_FILES)
    fun uploadFile(@Body file : RequestBody) : Call<List<String>>

    @GET(USER_ME)
    fun getUserInfo() : Single<User>

}
