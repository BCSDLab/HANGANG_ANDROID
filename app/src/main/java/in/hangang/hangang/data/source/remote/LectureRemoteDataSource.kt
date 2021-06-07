package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.constant.SORT_BY_LATEST_REVIEW
import `in`.hangang.hangang.constant.SORT_BY_REVIEW_COUNT
import `in`.hangang.hangang.constant.SORT_BY_TOTAL_RATING
import `in`.hangang.hangang.data.evaluation.*
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.ranking.RankingLectureResult
import `in`.hangang.hangang.data.request.LectureEvaluationRequest
import `in`.hangang.hangang.data.request.LectureReviewReportRequest
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureDataSource
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body

class LectureRemoteDataSource(private val noAuthApi: NoAuthApi, private val authApi: AuthApi) :
    LectureDataSource {

    override fun getLectureRankingByTotalRating(
        majors: ArrayList<String>,
        page: Int
    ): Single<RankingLectureResult> {
        return noAuthApi.getLectureRanking(
            department = majors,
            sort = SORT_BY_TOTAL_RATING,
            page = page
        )
    }

    override fun getLectureRankingByReviewCount(
        majors: ArrayList<String>,
        page: Int
    ): Single<RankingLectureResult> {
        return noAuthApi.getLectureRanking(
            department = majors,
            sort = SORT_BY_REVIEW_COUNT,
            page = page
        )
    }

    override fun getLectureRankingByLatestReview(
        majors: ArrayList<String>,
        page: Int
    ): Single<RankingLectureResult> {
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
    ): Single<RankingLectureResult> {
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

    override fun getRecommentedDocs(keyword: String): Single<LectureDocResult> {
        return authApi.getRecommentedDocs(keyword = keyword)
    }

    override fun getLectureReview(
        id: Int,
        page: Int,
        keyword: String?,
        sort: String
    ): Single<LectureReviewResult> {
        return authApi.getLectureReview(id = id, page = page, keyword = keyword, sort = sort)
    }

    override fun postReviewRecommend(reviewRecommendRequest: ReviewRecommendRequest): Single<CommonResponse> {
        return authApi.postReviewRecommend(reviewRecommendRequest)
    }

    override fun getLectureReviewItem(id: Int): Single<LectureReview> {
        return authApi.getLectureReviewItem(id)
    }

    override fun getLectureSemester(id: Int): Single<ArrayList<Int>> {
        return authApi.getLectureSemester(id)
    }

    override fun reportLectureReview(lectureReviewReportRequest: LectureReviewReportRequest): Single<CommonResponse> {
        return authApi.reportLectureReview(lectureReviewReportRequest)
    }

    override fun postEvaluation(lectureEvaluationRequest: LectureEvaluationRequest): Single<CommonResponse> {
        return authApi.postEvaluation(lectureEvaluationRequest)
    }
}