package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserRepository(
    private val userLocalDataSource: UserDataSource,
    private val userRemoteDataSource: UserDataSource
) : UserDataSource {
    override fun signUp(
        major: Array<String>,
        nickName: String,
        password: String,
        portalAccount: String
    ): Single<CommonResponse> {
        return userRemoteDataSource.signUp(major, nickName, password, portalAccount)
    }

    override fun checkAccessTokenValid(): Single<String> {
        return userRemoteDataSource.checkAccessTokenValid()
    }

    override fun login(portalID: String, password: String): Single<TokenResponse> {
        return userRemoteDataSource.login(portalID, password)
            .flatMap { userLocalDataSource.saveToken(it.accessToken!!, it.refreshToken!!) }
    }

    override fun updateToken(): Single<TokenResponse> {
        return userRemoteDataSource.updateToken()
            .flatMap { userLocalDataSource.saveToken(it.accessToken!!, it.refreshToken!!) }
    }

    override fun saveToken(accessToken: String, refreshToken: String): Single<TokenResponse> {
        return userLocalDataSource.saveToken(accessToken, refreshToken)
    }

    override fun emailCheck(portalAccount: String): Completable {
        return userRemoteDataSource.emailCheck(portalAccount)
    }

    override fun emailConfig(portalAccount: String, secret: String): Completable {
        return userRemoteDataSource.emailConfig(portalAccount, secret)
    }

    override fun checkNickname(nickName: String): Completable {
        return userRemoteDataSource.checkNickname(nickName)
    }

    override fun emailPasswordCheck(portalAccount: String): Completable {
        return userRemoteDataSource.emailPasswordCheck(portalAccount)
    }

    override fun emailPasswordConfig(portalAccount: String, secret: String): Completable {
        return userRemoteDataSource.emailPasswordConfig(portalAccount, secret)
    }

    override fun changePassword(portalAccount: String, password: String): Completable {
        return userRemoteDataSource.changePassword(portalAccount, password)
    }
}