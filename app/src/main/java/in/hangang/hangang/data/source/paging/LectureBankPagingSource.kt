package `in`.hangang.hangang.data.source.paging

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank

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