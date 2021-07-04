package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.lecturebank.LectureBankScrap
import `in`.hangang.hangang.data.response.CommonResponse
import io.reactivex.rxjava3.core.Single

interface LectureBankDataSource {
    fun getScrapLectureBanks() : Single<List<LectureBankScrap>>
    fun unscrapLectureBanks(scrapIds: List<Int>): Single<CommonResponse>
}