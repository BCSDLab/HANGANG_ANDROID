package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.source.LectureDataSource
import io.reactivex.rxjava3.core.Single

class LectureLocalDataSource : LectureDataSource {
    override fun getLectureRankingByTotalRating(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>> {
        return Single.never()
    }

    override fun getLectureRankingByReviewCount(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>> {
        return Single.never()
    }

    override fun getLectureRankingByLatestReview(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>> {
        return Single.never()
    }

    override fun getScrapedLecture(): Single<ArrayList<RankingLectureItem>> {
        return Single.never()
    }
}