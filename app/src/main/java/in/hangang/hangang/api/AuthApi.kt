package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.AUTH_TEST
import `in`.hangang.hangang.constant.LECTURE_BANKS
import `in`.hangang.hangang.constant.REFRESH
import `in`.hangang.hangang.data.lecturebank.LectureBankResult
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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
    @Query("page") page : Int) : Single<LectureBankResult>
}