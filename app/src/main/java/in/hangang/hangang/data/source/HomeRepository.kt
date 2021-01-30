package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.response.CommonResponse
import io.reactivex.rxjava3.core.Single

class HomeRepository(
        private val homeLocalDataSource: HomeDataSource,
        private val homeRemoteDataSource: HomeDataSource
) : HomeDataSource{
    override fun getLectureRanking(major: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun getTimetable(): Single<CommonResponse> {
        return Single.never()
    }

    override fun getRecommendedLectureData(): Single<CommonResponse> {
        return Single.never()
    }

    override fun getRecentLectures(): Single<CommonResponse> {
        return Single.never()
    }
}