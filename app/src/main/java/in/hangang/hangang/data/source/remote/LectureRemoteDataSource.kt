package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.constant.SORT_BY_LATEST_REVIEW
import `in`.hangang.hangang.constant.SORT_BY_REVIEW_COUNT
import `in`.hangang.hangang.constant.SORT_BY_TOTAL_RATING
import `in`.hangang.hangang.data.evaluation.Chart
import `in`.hangang.hangang.data.evaluation.ClassLecture
import `in`.hangang.hangang.data.evaluation.Evaluation
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.source.LectureDataSource
import io.reactivex.rxjava3.core.Single

class LectureRemoteDataSource(private val noAuthApi: NoAuthApi, private val authApi: AuthApi) :
    LectureDataSource {

    override fun getLectureRankingByTotalRating(
        majors: ArrayList<String>,
        page: Int
    ): Single<ArrayList<RankingLectureItem>> {
        return noAuthApi.getLectureRanking(
            department = majors,
            sort = SORT_BY_TOTAL_RATING,
            page = page
        )
    }

    override fun getLectureRankingByReviewCount(
        majors: ArrayList<String>,
        page: Int
    ): Single<ArrayList<RankingLectureItem>> {
        return noAuthApi.getLectureRanking(
            department = majors,
            sort = SORT_BY_REVIEW_COUNT,
            page = page
        )
    }

    override fun getLectureRankingByLatestReview(
        majors: ArrayList<String>,
        page: Int
    ): Single<ArrayList<RankingLectureItem>> {
        return noAuthApi.getLectureRanking(
            department = majors,
            sort = SORT_BY_LATEST_REVIEW,
            page = page
        )
    }

    override fun getScrapedLecture(): Single<ArrayList<RankingLectureItem>> {
        return authApi.getScrapedLecture()
    }

    override fun getFilteredLectureList(
        majors: ArrayList<String>,
        page: Int,
        filterType: ArrayList<String>?,
        filterHashTag: ArrayList<Int>?,
        sort: String,
        keyword: String?
    ): Single<ArrayList<RankingLectureItem>> {
        return noAuthApi.getLectureRanking(
            department = majors,
            sort = sort,
            classification = filterType,
            hashTag = filterHashTag,
            keyword = keyword,
            page = page
        )
    }

    override fun getEvaluationRating(id: Int): Single<ArrayList<Int>> {
        return authApi.getEvaluationRating(id)
    }

    override fun getClassLecture(id: Int): Single<ArrayList<ClassLecture>> {
        return authApi.getClassLectures(id)
    }

    override fun getEvaluationTotal(id: Int): Single<Evaluation> {
        return authApi.getEvalutaionTotal(id)
    }
}