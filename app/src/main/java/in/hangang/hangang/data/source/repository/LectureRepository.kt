package `in`.hangang.hangang.data.source.repository

import `in`.hangang.hangang.data.entity.evaluation.*
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.entity.ranking.RankingLectureResult
import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.request.LectureEvaluationIdRequest
import `in`.hangang.hangang.data.request.LectureEvaluationRequest
import `in`.hangang.hangang.data.request.LectureReviewReportRequest
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureDataSource
import `in`.hangang.hangang.data.source.paging.LectureReviewPagingSource
import `in`.hangang.hangang.data.source.paging.ReviewPagingSource
import androidx.compose.runtime.key
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

    override fun reportLectureReview(lectureReviewReportRequest: LectureReviewReportRequest): Single<CommonResponse> {
        return lectureRemoteDataSource.reportLectureReview(lectureReviewReportRequest)
    }

    override fun postEvaluation(lectureEvaluationRequest: LectureEvaluationRequest): Single<CommonResponse> {
        return lectureRemoteDataSource.postEvaluation(lectureEvaluationRequest)
    }

    override suspend fun getLectureClass(id: Int): ArrayList<ClassLecture> {
        return lectureRemoteDataSource.getLectureClass(id)
    }

    override suspend fun fetchClassLectures(id: Int): List<ClassLecture> {
        return lectureRemoteDataSource.fetchClassLectures(id)
    }

    override fun postScrapedLecture(scrapedLecture: LectureEvaluationIdRequest): Single<CommonResponse> {
        return lectureRemoteDataSource.postScrapedLecture(scrapedLecture)
    }

    override fun deleteScrapedLecture(scrapedLecture: ArrayList<Int>): Single<CommonResponse> {
        return lectureRemoteDataSource.deleteScrapedLecture(scrapedLecture)
    }

    override fun getLecturesId(id: Int): Single<RankingLectureItem> {
        return lectureRemoteDataSource.getLecturesId(id)
    }

    override fun getRecentlyLectureList(): ArrayList<RankingLectureItem> {
        return lectureLocalDataSource.getRecentlyLectureList()
    }

    override fun scrapLecture(lectureId: Int): Single<CommonResponse> {
        return lectureRemoteDataSource.scrapLecture(lectureId)
    }

    override fun unscrapLecture(vararg lectureId: Int): Single<CommonResponse> {
        return lectureRemoteDataSource.unscrapLecture(*lectureId)
    }

    override fun getScrapedLecture(): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getScrapedLecture()
    }
    override fun getLectureRankingByTotalRating(majors: ArrayList<String>, page: Int): Single<RankingLectureResult> {
        return lectureRemoteDataSource.getLectureRankingByTotalRating(majors, page)
    }

    override fun getLectureRankingByReviewCount(majors: ArrayList<String>, page: Int): Single<RankingLectureResult> {
        return lectureRemoteDataSource.getLectureRankingByReviewCount(majors, page)
    }

    override fun getLectureRankingByLatestReview(majors: ArrayList<String>, page: Int): Single<RankingLectureResult> {
        return lectureRemoteDataSource.getLectureRankingByLatestReview(majors, page)
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
    ): Flowable<PagingData<RankingLectureItem>> {
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

    fun getLectureReviewList(id: Int, keyword: String?, sort: String): Flowable<PagingData<LectureReview>> {
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

    override fun getLectureSemester(id: Int): Single<ArrayList<Int>> {
        return lectureRemoteDataSource.getLectureSemester(id)
    }

    fun getLectureReviewPersonalCount(id: Int, keyword: String?, sort: String): Single<LectureReviewResult> {
        return lectureRemoteDataSource.getLectureReview(id, 1, keyword, sort)
    }
    override fun getLectureList(
        classification: String?,
        hashTag: Int?,
        department: String?,
        keyword: String?,
        sort: String?
    ): Flowable<PagingData<Lecture>> {
        return lectureRemoteDataSource.getLectureList(classification, hashTag, department, keyword, sort)
    }
}