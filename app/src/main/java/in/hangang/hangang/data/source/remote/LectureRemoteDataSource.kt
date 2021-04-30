package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.constant.SORT_BY_LATEST_REVIEW
import `in`.hangang.hangang.constant.SORT_BY_REVIEW_COUNT
import `in`.hangang.hangang.constant.SORT_BY_TOTAL_RATING
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.source.LectureDataSource
import io.reactivex.rxjava3.core.Single

class LectureRemoteDataSource(private val noAuthApi: NoAuthApi) : LectureDataSource {

    override fun getLectureRankingByTotalRating(major: String, page: Int): Single<ArrayList<RankingLectureItem>> {
        return noAuthApi.getLectureRanking(department = major, sort = SORT_BY_TOTAL_RATING, page = page)
    }

    override fun getLectureRankingByReviewCount(major: String, page: Int): Single<ArrayList<RankingLectureItem>> {
        return noAuthApi.getLectureRanking(department = major, sort = SORT_BY_REVIEW_COUNT, page = page)
    }

    override fun getLectureRankingByLatestReview(major: String, page: Int): Single<ArrayList<RankingLectureItem>> {
        return noAuthApi.getLectureRanking(department = major, sort = SORT_BY_LATEST_REVIEW, page = page)
    }
}