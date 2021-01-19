package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.request.*
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface NoAuthApi {
    @POST(SIGN_UP)
    fun signUp(
        @Field("major") major: Array<String>,
        @Field("nickname") nickName: String,
        @Field("password") password: String,
        @Field("portal_account") portalAccount: String
    ): Single<CommonResponse>

    @POST(SEND_EMAIL)
    fun checkEmail(
        @Body emailRequest: EmailRequest
    ): Completable

    @POST(CONFIG_EMAIL)
    fun configEmail(
        @Body emailConfigRequest: EmailConfigRequest
    ): Completable

    @POST(SEND_PASSWORD_FIND_EMAIL)
    fun sendPasswordFindEmail(
        @Body emailRequest: EmailRequest
    ): Completable


    @POST(SEND_PASSWORD_CONFIG_EMAIL)
    fun sendPasswordConfigEmail(
        @Body emailConfigRequest: EmailConfigRequest
    ): Completable

    @POST(PASSWORD_FIND)
    fun passwordFind(
        @Body findPasswordFindRequest: PasswordFindRequest
    ): Completable

    @POST(CHECK_NICKNAME)
    fun checkNickName(
        @Body checkNickNameCheckRequest: NickNameCheckRequest
    ): Completable

    @POST(LOGIN)
    fun login(
        @Body loginRequest: LoginRequest
    ): Single<TokenResponse>

}