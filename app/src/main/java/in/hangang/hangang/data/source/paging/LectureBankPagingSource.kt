package `in`.hangang.hangang.data.source.paging

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.data.source.LectureBankDataSource
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.withThread
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single

const val DEFAULT_LIMIT = 30
const val START_PAGE = 1

class LectureBankPagingSource(
    private val authApi: AuthApi,
    private val categories: List<String>?,
    private val department: String?,
    private val keyword: String?,
    private val order: String
) : RxPagingSource<Int, LectureBank>() {
    override fun getRefreshKey(state: PagingState<Int, LectureBank>): Int? {
        if(state.anchorPosition == null) return null
        else {
            val anchorPage = state.closestPageToPosition(state.anchorPosition!!) ?: return null
            if(anchorPage.prevKey != null) return anchorPage.prevKey!! + 1
            if(anchorPage.nextKey != null) return anchorPage.nextKey!! - 1

            return null
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, LectureBank>> {
        val nextPageNumber = params.key ?: START_PAGE

        return authApi.getLectureBanks(
            categories, department, keyword, DEFAULT_LIMIT, order, nextPageNumber
        ).map { it.result }
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
        lectureBanks : List<LectureBank>,
        nextPageNumber: Int
    ) : LoadResult<Int, LectureBank> {
        return LoadResult.Page(
            data = lectureBanks,
            prevKey = if(nextPageNumber == START_PAGE) null else nextPageNumber - 1,
            nextKey = if(lectureBanks.isEmpty()) null else nextPageNumber + 1
        )
    }
}