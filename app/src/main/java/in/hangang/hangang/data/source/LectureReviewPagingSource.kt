package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

private const val LECTURE_REVIEW_STARTING_PAGE_INDEX = 1
private const val LECTURE_REVIEW_TOTAL = 61

class LectureReviewPagingSource(private val lectureDataSource: LectureDataSource, private val major: String) :
    RxPagingSource<Int, RankingLectureItem>() {
    override fun getRefreshKey(state: PagingState<Int, RankingLectureItem>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, RankingLectureItem>> {
        val nextPageNumber = params.key ?: LECTURE_REVIEW_STARTING_PAGE_INDEX

        return lectureDataSource.getLectureRankingByReviewCount(major = major, page = nextPageNumber)
            .handleHttpException()
            .subscribeOn(Schedulers.io())
            .map {
                toLoadResult(it, nextPageNumber)
            }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        data: ArrayList<RankingLectureItem>,
        nextPageNumber: Int
    ): LoadResult<Int, RankingLectureItem> {


        return LoadResult.Page(
            data = data,
            prevKey = if (nextPageNumber == LECTURE_REVIEW_STARTING_PAGE_INDEX) null else nextPageNumber - 1,
            nextKey = if (nextPageNumber < LECTURE_REVIEW_TOTAL) nextPageNumber + 1 else null
        )
    }


}
