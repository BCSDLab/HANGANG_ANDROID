package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.request.SaveMyProfileRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.MyProfileResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface AuthApi {
    @GET(AUTH_TEST)
    fun authCheck(): Single<CommonResponse>

    @POST(REFRESH)
    fun refreshToken(): Single<TokenResponse>

    @GET(MyProfile)
    fun setMyProfile(): Single<MyProfileResponse>

    @PUT(SaveMyProfile)
    fun saveMyProfile(
        @Body saveMyProfileRequest: SaveMyProfileRequest
    ): Single<CommonResponse>

    @HTTP(method = "DELETE", path = Delete_Account, hasBody = true)
    fun deleteAccount(): Single<CommonResponse>
}