package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.evaluation.*
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.response.CommonResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body

interface LectureDataSource {
    fun getLectureRankingByTotalRating(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>>
    fun getLectureRankingByReviewCount(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>>
    fun getLectureRankingByLatestReview(majors: ArrayList<String>, Page: Int): Single<ArrayList<RankingLectureItem>>
    fun getFilteredLectureList(majors: ArrayList<String>, page: Int, filterType: ArrayList<String>?, filterHashTag: ArrayList<Int>?, sort: String, keyword: String?): Single<ArrayList<RankingLectureItem>>
    fun getScrapedLecture(): Single<ArrayList<RankingLectureItem>>
    fun getEvaluationRating(id: Int): Single<ArrayList<Int>>
    fun getClassLecture(id: Int): Single<ArrayList<ClassLecture>>
    fun getEvaluationTotal(id: Int): Single<Evaluation>
    fun getRecommentedDocs(keyword: String): Single<LectureDocResult>
    fun getLectureReview(id: Int, page: Int, keyword: String?, sort: String): Single<ArrayList<LectureReview>>
    fun postReviewRecommend(reviewRecommendRequest: ReviewRecommendRequest): Single<CommonResponse>
    fun getLectureReviewItem(id: Int): Single<LectureReview>
}