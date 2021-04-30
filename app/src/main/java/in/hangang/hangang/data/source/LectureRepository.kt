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
    override fun getLectureRankingByTotalRating(major: String, page: Int): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getLectureRankingByTotalRating(major, page)
    }

    override fun getLectureRankingByReviewCount(major: String, page: Int): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getLectureRankingByReviewCount(major, page)
    }

    override fun getLectureRankingByLatestReview(major: String, page: Int): Single<ArrayList<RankingLectureItem>> {
        return lectureRemoteDataSource.getLectureRankingByLatestReview(major, page)
    }

    fun getLectureReviewList(major: String): Flowable<PagingData<RankingLectureItem>> {
        return Pager(PagingConfig(pageSize = 20)){
            LectureReviewPagingSource(lectureRemoteDataSource,major)
        }.flowable
    }
}