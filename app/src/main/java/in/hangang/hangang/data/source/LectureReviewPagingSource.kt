package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.util.handleHttpException
import androidx.compose.runtime.key
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

private const val LECTURE_REVIEW_STARTING_PAGE_INDEX = 1


class LectureReviewPagingSource(
    private val lectureDataSource: LectureDataSource,
    private val majors: ArrayList<String>,
    private var filterType: ArrayList<String>?,
    private var filterHashTag: ArrayList<Int>?,
    private var keyword: String?,
    private val sort: String
) :
    RxPagingSource<Int, RankingLectureItem>() {
    override fun getRefreshKey(state: PagingState<Int, RankingLectureItem>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, RankingLectureItem>> {
        val nextPageNumber = params.key ?: LECTURE_REVIEW_STARTING_PAGE_INDEX
        if(filterType?.size == 0)
            filterType = null
        if(filterHashTag?.size == 0)
            filterHashTag = null
        return lectureDataSource.getFilteredLectureList(
            majors = majors,
            page = nextPageNumber,
            filterType = filterType,
            filterHashTag = filterHashTag,
            sort = sort,
            keyword = keyword

        )
            .handleHttpException()
            .subscribeOn(Schedulers.io())
            .map {
                LECTURE_REVIEW_TOTAL = (it.count / 20) + 1
                toLoadResult(it.result, nextPageNumber)
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
    companion object{
        var LECTURE_REVIEW_TOTAL: Int = 30
    }


}
