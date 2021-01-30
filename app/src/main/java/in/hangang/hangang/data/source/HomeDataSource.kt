package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.response.CommonResponse
import io.reactivex.rxjava3.core.Single

interface HomeDataSource {
    fun getLectureRanking(major : String) : Single<CommonResponse>
    fun getTimetable() : Single<CommonResponse>
    fun getRecommendedLectureData() : Single<CommonResponse>
    fun getRecentLectures() : Single<CommonResponse>
}