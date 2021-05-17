package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.evaluation.Chart
import `in`.hangang.hangang.data.evaluation.ClassLecture
import `in`.hangang.hangang.data.evaluation.Evaluation
import `in`.hangang.hangang.data.ranking.RankingLectureItem
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
    override fun getLectureRankingByTotalRating(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getLectureRankingByTotalRating(majors, page)
    }

    override fun getLectureRankingByReviewCount(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getLectureRankingByReviewCount(majors, page)
    }

    override fun getLectureRankingByLatestReview(majors: ArrayList<String>, page: Int): Single<ArrayList<RankingLectureItem>> {
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
    ): Single<ArrayList<RankingLectureItem>> {
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
}