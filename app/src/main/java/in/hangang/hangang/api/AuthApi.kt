package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.request.LectureBankReportRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.LectureBankCommentResponse
import `in`.hangang.hangang.data.response.LectureBankResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface AuthApi {
    @GET(AUTH_TEST)
    fun authCheck(): Single<CommonResponse>

    @POST(REFRESH)
    fun refreshToken(): Single<TokenResponse>

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

}