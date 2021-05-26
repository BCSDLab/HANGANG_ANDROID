package `in`.hangang.hangang.data.source.paging

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.data.lecturebank.LectureBankComment

class LectureBankCommentPagingSource(
    private val authApi: AuthApi,
    private val lectureBankId: Int
) : BaseRxPagingSource<LectureBankComment>(
    singleApiFunction = { nextPageNumber ->
        authApi.getLectureBankComments(lectureBankId, DEFAULT_LIMIT, nextPageNumber)
            .map { it.comments }
    }
)