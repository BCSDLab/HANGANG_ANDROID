package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.lecture.RankingLecture
import io.reactivex.rxjava3.core.Single

interface LectureDataSource {
    fun getLectureRankingByTotalRating(major: String): Single<ArrayList<RankingLecture>>
    fun getLectureRankingByReviewCount(major: String): Single<ArrayList<RankingLecture>>
    fun getLectureRankingByLatestReview(major: String): Single<ArrayList<RankingLecture>>
}