package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankScrap
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureBankDataSource
import io.reactivex.rxjava3.core.Single

class LectureBankRemoteDataSource(
    private val authApi: AuthApi
) : LectureBankDataSource {
    override fun getScrapLectureBanks(): Single<List<LectureBankScrap>> {
        return authApi.getLectureBankScraps()
    }

    override fun unscrapLectureBanks(scrapIds: List<Int>): Single<CommonResponse> {
        return authApi.unscrapLectureBank(scrapIds)
    }
}