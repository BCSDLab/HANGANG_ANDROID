package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.ranking.RankingLectureItem
import io.reactivex.rxjava3.core.Single

class LectureRepository(
    private val lectureLocalDataSource: LectureDataSource,
    private val lectureRemoteDataSource: LectureDataSource
) : LectureDataSource {
    override fun getLectureRankingByTotalRating(major: String): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getLectureRankingByTotalRating(major)
    }

    override fun getLectureRankingByReviewCount(major: String): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getLectureRankingByReviewCount(major)
    }

    override fun getLectureRankingByLatestReview(major: String): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getLectureRankingByLatestReview(major)
    }
}