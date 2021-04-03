package `in`.hangang.hangang.api

import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.request.*
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NoAuthApi {
    @POST(SIGN_UP)
    fun signUp(
            @Body signUpRequest: SignUpRequest
    ): Single<CommonResponse>

    @POST(SEND_EMAIL)
    fun checkEmail(
            @Body emailRequest: EmailRequest
    ): Single<CommonResponse>

    @POST(CONFIG_EMAIL)
    fun configEmail(
            @Body emailConfigRequest: EmailConfigRequest
    ): Single<CommonResponse>

    @POST(SEND_PASSWORD_FIND_EMAIL)
    fun sendPasswordFindEmail(
            @Body emailRequest: EmailRequest
    ): Single<CommonResponse>

    @POST(SEND_PASSWORD_CONFIG_EMAIL)
    fun sendPasswordConfigEmail(
            @Body emailConfigRequest: EmailConfigRequest
    ): Single<CommonResponse>

    @POST(PASSWORD_FIND)
    fun passwordFind(
            @Body findPasswordFindRequest: PasswordFindRequest
    ): Single<CommonResponse>

    @POST(CHECK_NICKNAME)
    fun checkNickName(
            @Body checkNickNameCheckRequest: NickNameCheckRequest
    ): Single<CommonResponse>

    @POST(LOGIN)
    fun login(
            @Body loginRequest: LoginRequest
    ): Single<TokenResponse>

    @GET(LECTURES)
    fun getLectures(
            @Query("classification") classification: String? = null,
            @Query("department") department: String? = null,
            @Query("hash_tag") hashTag: Int? = null,
            @Query("keyword") keyword: String? = null,
            @Query("limit") limit: Int = 10,
            @Query("page") page: Int = 1,
            @Query("sort") sort: String? = null
    ): Single<List<Lecture>>
}