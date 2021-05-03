package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.ranking.RankingLectureItem
import io.reactivex.rxjava3.core.Single

interface LectureDataSource {
    fun getLectureRankingByTotalRating(major: String): Single<ArrayList<RankingLectureItem>>
    fun getLectureRankingByReviewCount(major: String): Single<ArrayList<RankingLectureItem>>
    fun getLectureRankingByLatestReview(major: String): Single<ArrayList<RankingLectureItem>>
}