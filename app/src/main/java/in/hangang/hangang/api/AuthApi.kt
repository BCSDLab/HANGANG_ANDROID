package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.AUTH_TEST
import `in`.hangang.hangang.constant.REFRESH
import `in`.hangang.hangang.constant.USER_LECTURE
import `in`.hangang.hangang.constant.USER_ME
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



}