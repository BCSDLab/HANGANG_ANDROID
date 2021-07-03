package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.data.entity.lecturebank.LectureBankScrap
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureBankDataSource
import io.reactivex.rxjava3.core.Single

class LectureBankLocalDataSource : LectureBankDataSource {
    override fun getScrapLectureBanks(): Single<List<LectureBankScrap>> {
        return Single.never()
    }

    override fun unscrapLectureBanks(scrapIds: List<Int>): Single<CommonResponse> {
        return Single.never()
    }
}