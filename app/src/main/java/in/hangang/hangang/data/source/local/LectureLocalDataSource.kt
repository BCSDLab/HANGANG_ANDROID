package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.entity.ranking.RankingLectureResult
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.constant.RECENTLY_READ_LECTURE_REVIEW
import `in`.hangang.hangang.data.request.LectureEvaluationIdRequest
import `in`.hangang.hangang.data.request.LectureEvaluationRequest
import `in`.hangang.hangang.data.request.LectureReviewReportRequest
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import com.orhanobut.hawk.Hawk
import `in`.hangang.hangang.data.entity.evaluation.ClassLecture
import `in`.hangang.hangang.data.entity.evaluation.Evaluation
import `in`.hangang.hangang.data.entity.evaluation.LectureDocResult
import `in`.hangang.hangang.data.entity.evaluation.LectureReview
import `in`.hangang.hangang.data.entity.evaluation.LectureReviewResult
import `in`.hangang.hangang.data.source.LectureDataSource
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class LectureLocalDataSource : LectureDataSource {
    override fun getLectureRankingByTotalRating(majors: ArrayList<String>, page: Int): Single<RankingLectureResult> {
        return Single.never()
    }

    override fun getLectureRankingByReviewCount(majors: ArrayList<String>, page: Int): Single<RankingLectureResult> {
        return Single.never()
    }

    override fun getLectureRankingByLatestReview(majors: ArrayList<String>, page: Int): Single<RankingLectureResult> {
        return Single.never()
    }

    override fun getScrapedLecture(): Single<ArrayList<RankingLectureItem>> {
        return Single.never()
    }

    override fun getFilteredLectureList(
        majors: ArrayList<String>,
        page: Int,
        filterType: ArrayList<String>?,
        filterHashTag: ArrayList<Int>?,
        sort: String,
        keyword: String?
    ): Single<RankingLectureResult> {
        return Single.never()
    }

    override fun getEvaluationRating(id: Int): Single<ArrayList<Int>> {
        return Single.never()
    }

    override fun getClassLecture(id: Int): Single<ArrayList<ClassLecture>> {
        return Single.never()
    }

    override fun getEvaluationTotal(id: Int): Single<Evaluation> {
        return Single.never()
    }

    override fun getRecommentedDocs(keyword: String): Single<LectureDocResult> {
        return Single.never()
    }

    override fun getLectureReview(
        id: Int,
        page: Int,
        keyword: String?,
        sort: String
    ): Single<LectureReviewResult> {
        return Single.never()
    }

    override fun postReviewRecommend(reviewRecommendRequest: ReviewRecommendRequest): Single<CommonResponse> {
        return Single.never()
    }

    override fun getLectureReviewItem(id: Int): Single<LectureReview> {
        return Single.never()
    }

    override fun getLectureSemester(id: Int): Single<ArrayList<Int>> {
        return Single.never()
    }

    override fun getLectureList(
        classification: String?,
        hashTag: Int?,
        department: String?,
        keyword: String?,
        sort: String?
    ): Flowable<PagingData<Lecture>> {
        return Flowable.never()
    }

    override fun scrapLecture(lectureId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun unscrapLecture(vararg lectureId: Int): Single<CommonResponse> {
        return Single.never()
    }
    override fun reportLectureReview(lectureReviewReportRequest: LectureReviewReportRequest): Single<CommonResponse> {
        return Single.never()
    }

    override fun postEvaluation(lectureEvaluationRequest: LectureEvaluationRequest): Single<CommonResponse> {
        return Single.never()
    }

    override suspend fun getLectureClass(id: Int): ArrayList<ClassLecture> {
        return arrayListOf()
    }

    override suspend fun fetchClassLectures(id: Int): List<ClassLecture> {
        return emptyList()
    }

    override fun postScrapedLecture(scrapedLecture: LectureEvaluationIdRequest): Single<CommonResponse> {
        return Single.never()
    }

    override fun deleteScrapedLecture(scrapedLecture: ArrayList<Int>): Single<CommonResponse> {
        return Single.never()
    }

    override fun getLecturesId(id: Int): Single<RankingLectureItem> {
        return Single.never()
    }

    override fun getRecentlyLectureList(): ArrayList<RankingLectureItem> {
        return Hawk.get(RECENTLY_READ_LECTURE_REVIEW, ArrayList<RankingLectureItem>())

    }
}
