package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.ranking.RankingLectureItem
import io.reactivex.rxjava3.core.Single

interface LectureDataSource {
    fun getLectureRankingByTotalRating(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>>
    fun getLectureRankingByReviewCount(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>>
    fun getLectureRankingByLatestReview(majors: ArrayList<String>, Page: Int): Single<ArrayList<RankingLectureItem>>
    fun getScrapedLecture(): Single<ArrayList<RankingLectureItem>>
}