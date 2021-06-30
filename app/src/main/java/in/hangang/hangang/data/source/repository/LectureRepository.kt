package `in`.hangang.hangang.data.source.repository

import `in`.hangang.hangang.data.entity.evaluation.*
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.entity.ranking.RankingLectureResult
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureDataSource
import `in`.hangang.hangang.data.source.paging.LectureReviewPagingSource
import `in`.hangang.hangang.data.source.paging.ReviewPagingSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class LectureRepository(
    private val lectureLocalDataSource: LectureDataSource,
    private val lectureRemoteDataSource: LectureDataSource
) : LectureDataSource {
    override fun getLectureRankingByTotalRating(majors: ArrayList<String>, page: Int): Single<RankingLectureResult> {
        return lectureRemoteDataSource.getLectureRankingByTotalRating(majors, page)
    }

    override fun getLectureRankingByReviewCount(majors: ArrayList<String>, page: Int): Single<RankingLectureResult> {
        return lectureRemoteDataSource.getLectureRankingByReviewCount(majors, page)
    }

    override fun getLectureRankingByLatestReview(majors: ArrayList<String>, page: Int): Single<RankingLectureResult> {
        return lectureRemoteDataSource.getLectureRankingByLatestReview(majors, page)
    }

    override fun getScrapedLecture(): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getScrapedLecture()
    }

    override fun getFilteredLectureList(
        majors: ArrayList<String>,
        page: Int,
        filterType: ArrayList<String>?,
        filterHashTag: ArrayList<Int>?,
        sort: String,
        keyword: String?
    ): Single<RankingLectureResult> {
        return lectureRemoteDataSource.getFilteredLectureList(majors,page,filterType,filterHashTag,sort,keyword)
    }

    override fun getClassLecture(id: Int): Single<ArrayList<ClassLecture>> {
        return lectureRemoteDataSource.getClassLecture(id)
    }

    fun getFilteredLectureReviewList(
        majors: ArrayList<String>,
        filterType: ArrayList<String>?,
        filterHashTag: ArrayList<Int>?,
        sort: String,
        keyword: String?
    ):Flowable<PagingData<RankingLectureItem>> {
        return Pager(PagingConfig(pageSize = 20)){
            LectureReviewPagingSource(lectureRemoteDataSource,majors, filterType, filterHashTag, keyword, sort)
        }.flowable
    }

    override fun getEvaluationRating(id: Int): Single<ArrayList<Int>> {
        return lectureRemoteDataSource.getEvaluationRating(id)
    }

    override fun getEvaluationTotal(id: Int): Single<Evaluation> {
        return lectureRemoteDataSource.getEvaluationTotal(id)
    }

    override fun getRecommentedDocs(keyword: String): Single<LectureDocResult> {
        return lectureRemoteDataSource.getRecommentedDocs(keyword)
    }

    override fun getLectureReview(
        id: Int,
        page: Int,
        keyword: String?,
        sort: String
    ): Single<LectureReviewResult> {
        return lectureRemoteDataSource.getLectureReview(id, page, keyword, sort)
    }

    fun getLectureReviewList(id: Int, keyword: String?, sort: String):Flowable<PagingData<LectureReview>> {
        return Pager(PagingConfig(pageSize = 20)){
            ReviewPagingSource(lectureRemoteDataSource,id, keyword, sort)
        }.flowable
    }

    override fun postReviewRecommend(reviewRecommendRequest: ReviewRecommendRequest): Single<CommonResponse> {
        return lectureRemoteDataSource.postReviewRecommend(reviewRecommendRequest)
    }

    override fun getLectureReviewItem(id: Int): Single<LectureReview> {
        return lectureRemoteDataSource.getLectureReviewItem(id)
    }

    override fun getLectureSemester(id: Int): Single<ArrayList<String>> {
        return lectureRemoteDataSource.getLectureSemester(id)
    }
}