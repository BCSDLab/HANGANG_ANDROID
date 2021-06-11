package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.constant.ACCESS_TOKEN
import `in`.hangang.hangang.constant.REFRESH_TOKEN
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.MyProfileResponse
import `in`.hangang.hangang.data.response.TokenResponse
import `in`.hangang.hangang.data.source.source.UserDataSource
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserLocalDataSource : UserDataSource {
    override fun signUp(
            major: Array<String>,
            nickName: String,
            password: String,
            portalAccount: String
    ): Single<CommonResponse> {
        return Single.never()
    }

    override fun checkAccessTokenValid(): Single<CommonResponse> {
        return Single.never()
    }

    override fun login(portalID: String, password: String): Single<TokenResponse> {
        return Single.never()
    }

    override fun updateToken(): Single<TokenResponse> {
        return Single.never()
    }

    override fun saveToken(accessToken: String, refreshToken: String): Single<TokenResponse> {
        return Single.create { subscriber ->
            if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty())
                subscriber.onError(Throwable("Token Empty"))
            Hawk.put(ACCESS_TOKEN, accessToken)
            Hawk.put(REFRESH_TOKEN, refreshToken)
            val tokenResponse = TokenResponse()
            tokenResponse.apply {
                this.accessToken = accessToken
                this.refreshToken = accessToken
            }
            subscriber.onSuccess(tokenResponse)
        }
    }

    override fun emailCheck(portalAccount: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun emailConfig(portalAccount: String, secret: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun checkNickname(nickName: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun emailPasswordCheck(portalAccount: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun emailPasswordConfig(
            portalAccount: String,
            secret: String
    ): Single<CommonResponse> {
        return Single.never()
    }

    override fun changePassword(portalAccount: String, password: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun deleteAccount(): Single<CommonResponse> {
        return Single.never()
    }

    override fun logoutAll(): Single<CommonResponse> {
        return Single.never()
    }

    override fun saveAutoLogin(isAutoLogin: Boolean): Completable {
        TODO("Not yet implemented")
    }

    override fun getAutoLoginStatus(): Single<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getMyProfile(): Single<MyProfileResponse> {
        return Single.never()
    }

    override fun saveProfile(name: String, nickName: String, major: Array<String>): Single<CommonResponse> {
        return  Single.never()
    }
}