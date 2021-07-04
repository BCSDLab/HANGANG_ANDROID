package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.evaluation.LectureDoc
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.MyProfileResponse
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

    fun saveProfile(name: String, nickName: String, major: ArrayList<String>): Single<CommonResponse>

    fun deleteAccount(): Single<CommonResponse>

    fun logoutAll(): Single<CommonResponse>

    fun saveAutoLogin(isAutoLogin: Boolean): Completable

    fun getAutoLoginStatus(): Single<Boolean>

    fun getMyProfile(): Single<MyProfileResponse>

    fun getLectureBankHit(): Single<List<LectureDoc>>
}