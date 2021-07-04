package `in`.hangang.hangang.data.source.repository

import `in`.hangang.hangang.data.entity.lecturebank.LectureBankScrap
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureBankDataSource
import io.reactivex.rxjava3.core.Single

class LectureBankRepository(
    private val lectureBankLocalDataSource: LectureBankDataSource,
    private val lectureBankRemoteDataSource: LectureBankDataSource
) : LectureBankDataSource {
    override fun getScrapLectureBanks(): Single<List<LectureBankScrap>> {
        return lectureBankRemoteDataSource.getScrapLectureBanks()
    }

    override fun unscrapLectureBanks(scrapIds: List<Int>): Single<CommonResponse> {
        return lectureBankRemoteDataSource.unscrapLectureBanks(scrapIds)
    }
}