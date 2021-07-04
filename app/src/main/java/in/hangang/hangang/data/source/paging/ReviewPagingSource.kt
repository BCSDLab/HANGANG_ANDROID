package `in`.hangang.hangang.data.source.paging

import `in`.hangang.hangang.data.entity.evaluation.LectureReview
import `in`.hangang.hangang.data.source.LectureDataSource
import `in`.hangang.hangang.util.handleHttpException
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

private const val LECTURE_REVIEW_STARTING_PAGE_INDEX = 1
class ReviewPagingSource (
    private val lectureDataSource: LectureDataSource,
    private val id: Int,
    private var keyword: String?,
    private var sort: String

): RxPagingSource<Int, LectureReview>() {
    override fun getRefreshKey(state: PagingState<Int, LectureReview>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, LectureReview>> {
        val nextPageNumber = params.key ?: LECTURE_REVIEW_STARTING_PAGE_INDEX
        return lectureDataSource.getLectureReview(
            id = id,
            page = nextPageNumber,
            keyword = keyword,
            sort = sort
        )
            .handleHttpException()
            .subscribeOn(Schedulers.io())
            .map{
                LECTURE_REVIEW_TOTAL = (it.count / 20) + 1
                PERSONAL_REVIEW_COUNT = it.count
                toLoadResult(it.result, nextPageNumber)
            }
            .onErrorReturn { LoadResult.Error(it) }
    }
    private fun toLoadResult(
        data: ArrayList<LectureReview>,
        nextPageNumber: Int
    ): LoadResult<Int, LectureReview> {


        return LoadResult.Page(
            data = data,
            prevKey = if (nextPageNumber == LECTURE_REVIEW_STARTING_PAGE_INDEX) null else nextPageNumber - 1,
            nextKey = if (nextPageNumber < LECTURE_REVIEW_TOTAL) nextPageNumber + 1 else null
        )
    }
    companion object{
        var LECTURE_REVIEW_TOTAL: Int = 30
        var PERSONAL_REVIEW_COUNT: Int = 0
    }
}