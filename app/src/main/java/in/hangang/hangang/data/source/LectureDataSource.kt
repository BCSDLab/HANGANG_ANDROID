package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.evaluation.*
import `in`.hangang.hangang.data.entity.ranking.RankingLectureResult
import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.response.CommonResponse
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import `in`.hangang.hangang.data.request.LectureEvaluationIdRequest
import `in`.hangang.hangang.data.request.LectureEvaluationRequest
import `in`.hangang.hangang.data.request.LectureReviewReportRequest
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem

interface LectureDataSource {
    fun reportLectureReview(lectureReviewReportRequest: LectureReviewReportRequest): Single<CommonResponse>
    fun postEvaluation(lectureEvaluationRequest: LectureEvaluationRequest): Single<CommonResponse>
    suspend fun getLectureClass(id: Int): ArrayList<ClassLecture>
    suspend fun fetchClassLectures(id: Int): List<ClassLecture>
    fun postScrapedLecture(scrapedLecture: LectureEvaluationIdRequest): Single<CommonResponse>
    fun deleteScrapedLecture(scrapedLecture: ArrayList<Int>): Single<CommonResponse>
    fun getLecturesId(id: Int): Single<RankingLectureItem>
    fun getRecentlyLectureList(): ArrayList<RankingLectureItem>
    fun scrapLecture(lectureId: Int): Single<CommonResponse>
    fun unscrapLecture(vararg lectureId: Int): Single<CommonResponse>
    fun getLectureRankingByTotalRating(majors: ArrayList<String>, page: Int): Single<RankingLectureResult>
    fun getLectureRankingByReviewCount(majors: ArrayList<String>, page: Int): Single<RankingLectureResult>
    fun getLectureRankingByLatestReview(majors: ArrayList<String>, Page: Int): Single<RankingLectureResult>
    fun getFilteredLectureList(majors: ArrayList<String>, page: Int, filterType: ArrayList<String>?, filterHashTag: ArrayList<Int>?, sort: String, keyword: String?): Single<RankingLectureResult>
    fun getScrapedLecture(): Single<ArrayList<RankingLectureItem>>
    fun getEvaluationRating(id: Int): Single<ArrayList<Int>>
    fun getClassLecture(id: Int): Single<ArrayList<ClassLecture>>
    fun getEvaluationTotal(id: Int): Single<Evaluation>
    fun getRecommentedDocs(keyword: String): Single<LectureDocResult>
    fun getLectureReview(id: Int, page: Int, keyword: String?, sort: String): Single<LectureReviewResult>
    fun postReviewRecommend(reviewRecommendRequest: ReviewRecommendRequest): Single<CommonResponse>
    fun getLectureReviewItem(id: Int): Single<LectureReview>
    fun getLectureSemester(id: Int): Single<ArrayList<Int>>
    fun getLectureList(
        classification: String? = null,
        hashTag: Int? = null,
        department: String? = null,
        keyword: String? = null,
        sort: String? = null
    ): Flowable<PagingData<Lecture>>
}