package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.PointRecord
import `in`.hangang.hangang.data.entity.LectureBank
import `in`.hangang.hangang.data.entity.User
import `in`.hangang.hangang.data.entity.UserCount
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
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
}