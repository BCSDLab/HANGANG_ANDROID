package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.email.Email
import `in`.hangang.hangang.data.entity.evaluation.LectureDoc
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.mypage.PointRecord
import `in`.hangang.hangang.data.entity.user.User
import `in`.hangang.hangang.data.entity.user.UserCount
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.MyProfileResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface UserDataSource {
    fun signUp(
        major: ArrayList<String>,
        nickName: String,
        password: String,
        portalAccount: String
    ): Single<CommonResponse>

    fun checkAccessTokenValid(): Single<CommonResponse>

    fun login(portalID: String, password: String): Single<TokenResponse>

    fun updateToken(): Single<TokenResponse>

    fun saveToken(accessToken: String, refreshToken: String): Single<TokenResponse>

    fun emailCheck(portalAccount: String): Single<Email>

    fun emailConfig(portalAccount: String, secret: String): Single<CommonResponse>

    fun checkNickname(nickName: String): Single<CommonResponse>

    fun emailPasswordCheck(portalAccount: String): Single<CommonResponse>

    fun emailPasswordConfig(portalAccount: String, secret: String): Single<CommonResponse>

    fun changePassword(portalAccount: String, password: String): Single<CommonResponse>

    fun saveProfile(nickName: String, major: ArrayList<String>): Single<CommonResponse>

    fun deleteAccount(): Single<CommonResponse>

    fun logoutAll(): Single<CommonResponse>

    fun saveAutoLogin(isAutoLogin: Boolean): Completable

    fun getAutoLoginStatus(): Single<Boolean>

    fun getMyProfile(): Single<MyProfileResponse>

    fun getLectureBankHit(): Single<List<LectureDoc>>
  
    fun getUserInformation(): Single<User>

    fun getUserCounts(): Single<UserCount>

    fun getPointRecords(): Single<List<PointRecord>>

    fun getPurchasedBanks(): Single<List<LectureBank>>
    fun getUserInfo(): Single<User>
    fun saveUserInfo(user: User)
}
