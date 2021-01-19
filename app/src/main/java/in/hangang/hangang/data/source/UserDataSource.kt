package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface UserDataSource {
    fun signUp(
        major: Array<String>,
        nickName: String,
        password: String,
        portalAccount: String
    ): Single<CommonResponse>

    fun checkAccessTokenValid(): Single<String>

    fun login(portalID: String, password: String): Single<TokenResponse>

    fun updateToken(): Single<TokenResponse>

    fun saveToken(accessToken: String, refreshToken: String): Single<TokenResponse>

    fun emailCheck(portalAccount: String): Completable

    fun emailConfig(portalAccount: String, secret: String): Completable

    fun checkNickname(nickName: String): Completable

    fun emailPasswordCheck(portalAccount: String): Completable

    fun emailPasswordConfig(portalAccount: String, secret: String): Completable

    fun changePassword(portalAccount: String, password: String): Completable
}