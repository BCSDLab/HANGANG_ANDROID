package `in`.hangang.hangang.data.source.repository

import `in`.hangang.hangang.data.entity.evaluation.LectureDoc
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.mypage.PointRecord
import `in`.hangang.hangang.data.entity.user.User
import `in`.hangang.hangang.data.entity.user.UserCount
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.TokenResponse
import `in`.hangang.hangang.data.source.UserDataSource
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

    override fun checkAccessTokenValid(): Single<CommonResponse> {
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

    override fun emailCheck(portalAccount: String): Single<CommonResponse> {
        return userRemoteDataSource.emailCheck(portalAccount)
    }

    override fun emailConfig(portalAccount: String, secret: String): Single<CommonResponse> {
        return userRemoteDataSource.emailConfig(portalAccount, secret)
    }

    override fun checkNickname(nickName: String): Single<CommonResponse> {
        return userRemoteDataSource.checkNickname(nickName)
    }

    override fun emailPasswordCheck(portalAccount: String): Single<CommonResponse> {
        return userRemoteDataSource.emailPasswordCheck(portalAccount)
    }

    override fun emailPasswordConfig(
        portalAccount: String,
        secret: String
    ): Single<CommonResponse> {
        return userRemoteDataSource.emailPasswordConfig(portalAccount, secret)
    }

    override fun changePassword(portalAccount: String, password: String): Single<CommonResponse> {
        return userRemoteDataSource.changePassword(portalAccount, password)
    }

    override fun getLectureBankHit(): Single<List<LectureDoc>> {
        return userRemoteDataSource.getLectureBankHit()
    }

    override fun getUserInformation(): Single<User> {
        return userRemoteDataSource.getUserInformation()
    }

    override fun getUserCounts(): Single<UserCount> {
        return userRemoteDataSource.getUserCounts()
    }

    override fun getPointRecords(): Single<List<PointRecord>> {
        return userRemoteDataSource.getPointRecords()
    }

    override fun getPurchasedBanks(): Single<List<LectureBank>> {
        return userRemoteDataSource.getPurchasedBanks()
    }
}