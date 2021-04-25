package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.constant.SORT_BY_LATEST_REVIEW
import `in`.hangang.hangang.constant.SORT_BY_REVIEW_COUNT
import `in`.hangang.hangang.constant.SORT_BY_TOTAL_RATING
import `in`.hangang.hangang.data.lecture.RankingLecture
import io.reactivex.rxjava3.core.Single

class LectureRepository(
        private val lectureLocalDataSource: LectureDataSource,
        private val lectureRemoteDataSource: LectureDataSource
) : LectureDataSource{
    override fun getLectureRankingByTotalRating( major: String): Single<ArrayList<RankingLecture>> {
        return lectureRemoteDataSource.getLectureRankingByTotalRating(major)
    }

    override fun getLectureRankingByReviewCount(major: String): Single<ArrayList<RankingLecture>> {
        return lectureRemoteDataSource.getLectureRankingByReviewCount(major)
    }

    override fun getLectureRankingByLatestReview( major: String): Single<ArrayList<RankingLecture>> {
        return lectureRemoteDataSource.getLectureRankingByLatestReview(major)
    }
}