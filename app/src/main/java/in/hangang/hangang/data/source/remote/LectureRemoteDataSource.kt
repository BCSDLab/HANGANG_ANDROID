package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.constant.SORT_BY_LATEST_REVIEW
import `in`.hangang.hangang.constant.SORT_BY_REVIEW_COUNT
import `in`.hangang.hangang.constant.SORT_BY_TOTAL_RATING
import `in`.hangang.hangang.data.entity.timetable.Lecture
import `in`.hangang.hangang.data.entity.evaluation.*
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.entity.ranking.RankingLectureResult
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.paging.BaseRxPagingSource
import `in`.hangang.hangang.data.source.paging.LectureListPagingSource
import `in`.hangang.hangang.data.source.LectureDataSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable

import io.reactivex.rxjava3.core.Single

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

    override fun getLectureSemester(id: Int): Single<ArrayList<String>> {
        return authApi.getLectureSemester(id)
    }

    override fun getLectureList(
        classification: String?,
        department: String?,
        hashTag: Int?,
        keyword: String?,
        sort: String?
    ): Flowable<PagingData<Lecture>> {
        return Pager(PagingConfig(pageSize = BaseRxPagingSource.DEFAULT_LIMIT)) {
            LectureListPagingSource(noAuthApi, classification, department, hashTag, keyword, sort)
        }.flowable
    }
}