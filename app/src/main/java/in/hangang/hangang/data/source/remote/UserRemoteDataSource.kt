package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.data.request.*
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.MyProfileResponse
import `in`.hangang.hangang.data.response.TokenResponse
import `in`.hangang.hangang.data.source.source.UserDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserRemoteDataSource(
        private val noAuthApi: NoAuthApi,
        private val authApi: AuthApi,
        private val refreshApi: AuthApi
) :
        UserDataSource {
    override fun signUp(
            major: Array<String>,
            nickName: String,
            password: String,
            portalAccount: String
    ): Single<CommonResponse> {
        return noAuthApi.signUp(SignUpRequest(major, nickName, password, portalAccount))
    }

    override fun checkAccessTokenValid(): Single<CommonResponse> {
        return authApi.authCheck()
    }

    override fun login(portalID: String, password: String): Single<TokenResponse> {
        return noAuthApi.login(LoginRequest(portalID, password))
    }

    override fun updateToken(): Single<TokenResponse> {
        return refreshApi.refreshToken()
    }

    override fun saveToken(accessToken: String, refreshToken: String): Single<TokenResponse> {
        return Single.never()
    }


    override fun emailCheck(portalAccount: String): Single<CommonResponse> {
        return noAuthApi.checkEmail(EmailRequest(0, portalAccount))
    }

    override fun emailConfig(portalAccount: String, secret: String): Single<CommonResponse> {
        return noAuthApi.configEmail(EmailConfigRequest(0, portalAccount, secret))
    }

    override fun checkNickname(nickName: String): Single<CommonResponse> {
        return noAuthApi.checkNickName(NickNameCheckRequest(nickName))
    }

    override fun emailPasswordCheck(portalAccount: String): Single<CommonResponse> {
        return noAuthApi.sendPasswordFindEmail(EmailRequest(1, portalAccount))
    }

    override fun emailPasswordConfig(
        portalAccount: String,
        secret: String
    ): Single<CommonResponse> {
        return noAuthApi.sendPasswordConfigEmail(EmailConfigRequest(1, portalAccount, secret))
    }

    override fun changePassword(portalAccount: String, password: String): Single<CommonResponse> {
        return noAuthApi.passwordFind(PasswordFindRequest(portalAccount, password))
    }

    override fun deleteAccount(): Single<CommonResponse> {
        return authApi.deleteAccount()
    }

    override fun saveAutoLogin(isAutoLogin: Boolean): Completable {
        return Completable.never()
    }

    override fun getAutoLoginStatus(): Single<Boolean> {
        return Single.never()
    }

    override fun getMyProfile(): Single<MyProfileResponse> {
        return authApi.setMyProfile()
    }

    override fun saveProfile(name: String, nickName: String, major: Array<String>): Single<CommonResponse> {
        return authApi.saveMyProfile(SaveMyProfileRequest(name, nickName, major))
    }


}