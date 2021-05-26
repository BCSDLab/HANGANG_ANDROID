package `in`.hangang.hangang.data.source.paging

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.data.source.LectureBankDataSource
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.withThread
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single

class LectureBankPagingSource(
    private val authApi: AuthApi,
    private val categories: List<String>?,
    private val department: String?,
    private val keyword: String?,
    private val order: String
) : BaseRxPagingSource<LectureBank>(
    singleApiFunction = { nextPageNumber ->
        authApi.getLectureBanks(
            categories, department, keyword, DEFAULT_LIMIT, order, nextPageNumber
        ).map { it.result }
    }
)