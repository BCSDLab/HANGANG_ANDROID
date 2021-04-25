package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.data.lecture.RankingLecture
import `in`.hangang.hangang.data.source.LectureDataSource
import io.reactivex.rxjava3.core.Single

class LectureLocalDataSource : LectureDataSource {
    override fun getLectureRankingByTotalRating(major: String): Single<ArrayList<RankingLecture>> {
        return Single.never()
    }

    override fun getLectureRankingByReviewCount(major: String): Single<ArrayList<RankingLecture>> {
        return Single.never()
    }

    override fun getLectureRankingByLatestReview(major: String): Single<ArrayList<RankingLecture>> {
        return Single.never()
    }
}