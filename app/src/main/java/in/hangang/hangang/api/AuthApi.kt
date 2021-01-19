package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.AUTH_TEST
import `in`.hangang.hangang.constant.REFRESH
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @GET(AUTH_TEST)
    fun authCheck(): Single<String>

    @POST(REFRESH)
    fun refreshToken(): Single<TokenResponse>
}