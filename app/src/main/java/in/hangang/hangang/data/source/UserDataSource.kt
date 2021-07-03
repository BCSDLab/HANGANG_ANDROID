package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Single

interface UserDataSource {
    fun signUp(
            major: Array<String>,
            nickName: String,
            password: String,
            portalAccount: String
    ): Single<CommonResponse>

    fun checkAccessTokenValid(): Single<CommonResponse>

    fun login(portalID: String, password: String): Single<TokenResponse>

    fun updateToken(): Single<TokenResponse>

    fun saveToken(accessToken: String, refreshToken: String): Single<TokenResponse>

    fun emailCheck(portalAccount: String): Single<CommonResponse>

    fun emailConfig(portalAccount: String, secret: String): Single<CommonResponse>

    fun checkNickname(nickName: String): Single<CommonResponse>

    fun emailPasswordCheck(portalAccount: String): Single<CommonResponse>

    fun emailPasswordConfig(portalAccount: String, secret: String): Single<CommonResponse>

    fun changePassword(portalAccount: String, password: String): Single<CommonResponse>
}