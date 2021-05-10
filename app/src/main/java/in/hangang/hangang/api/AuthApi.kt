package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.*
import `in`.hangang.hangang.data.request.ScrapLectureRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface AuthApi {
    @GET(AUTH_TEST)
    fun authCheck(): Single<CommonResponse>

    @POST(REFRESH)
    fun refreshToken(): Single<TokenResponse>

    @GET(USER_ME)
    fun getUserInformation(): Single<User>

    @GET(USER_LECTURE)
    fun getUserCounts(): Single<UserCount>

    @GET(USER_POINT_RECORD)
    fun getUserPointRecord(): Single<List<PointRecord>>

    @GET(USER_PURCHASED)
    fun getUserPurchasedBanks(): Single<List<LectureBank>>

    @GET(LECTURE_SCRAPED)
    fun getScrapedLecture(): Single<List<Lecture>>

    @POST(LECTURE_SCRAPED)
    fun addScrapLecture(@Body scrapLectureRequest: ScrapLectureRequest): Single<CommonResponse>

    @HTTP(method = "DELETE", path = LECTURE_SCRAPED, hasBody = true)
    fun removeScrapLecture(@Body lectureIds: List<Int>): Single<CommonResponse>

}