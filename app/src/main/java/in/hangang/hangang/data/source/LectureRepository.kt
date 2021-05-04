package `in`.hangang.hangang.data.source

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

    fun getLectureReviewList(majors: ArrayList<String>): Flowable<PagingData<RankingLectureItem>> {
        return Pager(PagingConfig(pageSize = 20)){
            LectureReviewPagingSource(lectureRemoteDataSource,majors)
        }.flowable
    }
}