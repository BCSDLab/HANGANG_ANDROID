package `in`.hangang.hangang.data.source.paging

import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.data.entity.Lecture

class LectureListPagingSource(
    private val noAuthApi: NoAuthApi,
    private val classification: String? = null,
    private val department: String? = null,
    private val hashTagId: Int? = null,
    private val keyword: String? = null,
    private val sort: String? = null
) : BaseRxPagingSource<Lecture>(
    singleApiFunction = {
        noAuthApi.getLectures(
            classification, department, hashTagId, keyword, DEFAULT_LIMIT, it, sort
        ).map { it.result }
    }
)