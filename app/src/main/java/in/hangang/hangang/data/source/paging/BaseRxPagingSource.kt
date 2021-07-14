package `in`.hangang.hangang.data.source.paging

import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.withThread
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single

open class BaseRxPagingSource<V : Any>(
    private inline val singleApiFunction : (Int) -> Single<List<V>>,
    private val limit: Int = DEFAULT_LIMIT,
    private val startPage: Int = START_PAGE
): RxPagingSource<Int, V>() {
    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        if(state.anchorPosition == null) return null
        else {
            val anchorPage = state.closestPageToPosition(state.anchorPosition!!) ?: return null
            if(anchorPage.prevKey != null) return anchorPage.prevKey!! + 1
            if(anchorPage.nextKey != null) return anchorPage.nextKey!! - 1

            return null
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, V>> {
        val nextPageNumber = params.key ?: startPage

        return singleApiFunction(nextPageNumber)
            .withThread()
            .handleHttpException()
            .map {
                toLoadResult(it, nextPageNumber)
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(
        items : List<V>,
        nextPageNumber: Int
    ) : LoadResult<Int, V> {
        return LoadResult.Page(
            data = items,
            prevKey = if(nextPageNumber == startPage) null else nextPageNumber - 1,
            nextKey = if(items.isEmpty()) null else nextPageNumber + 1
        )
    }

    companion object {
        const val DEFAULT_LIMIT = 20
        const val START_PAGE = 1
    }
}