package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.data.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.request.LectureBankReportRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureBankDataSource
import `in`.hangang.hangang.data.source.paging.BaseRxPagingSource.Companion.DEFAULT_LIMIT
import `in`.hangang.hangang.data.source.paging.LectureBankCommentPagingSource
import `in`.hangang.hangang.data.source.paging.LectureBankPagingSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class LectureBankRemoteDataSource(
    private val authApi: AuthApi
) : LectureBankDataSource {
    override fun getLectureBanks(
        categories: List<String>?,
        department: String?,
        keyword: String?,
        order: String
    ): Flowable<PagingData<LectureBank>> {
        return Pager(PagingConfig(pageSize = DEFAULT_LIMIT)) {
            LectureBankPagingSource(authApi, categories, department, keyword, order)
        }.flowable
    }

    override fun getLectureBankComments(lectureBankId: Int): Flowable<PagingData<LectureBankComment>> {
        return Pager(PagingConfig(pageSize = DEFAULT_LIMIT)) {
            LectureBankCommentPagingSource(authApi, lectureBankId)
        }.flowable
    }

    override fun getLectureBankDetail(id: Int): Single<LectureBankDetail> {
        return authApi.getLectureBankDetail(id)
    }

    override fun hitLectureBank(id: Int): Single<CommonResponse> {
        return authApi.hitLectureBank(id)
    }

    override fun purchaseLectureBank(id: Int): Single<CommonResponse> {
        return authApi.purchaseLectureBank(id)
    }

    override fun checkLectureBankPurchased(id: Int): Single<Boolean> {
        return authApi.checkLectureBankPurchased(id)
    }

    override fun scrapLectureBank(lectureBankId: Int): Single<Int> {
        return authApi.scrapLectureBank(lectureBankId)
    }

    override fun unscrapLectureBank(scrapId: Int): Single<CommonResponse> {
        return authApi.unscrapLectureBanks(listOf(scrapId))
    }

    override fun unscrapLectureBanks(scrapIds: List<Int>): Single<CommonResponse> {
        return authApi.unscrapLectureBanks(scrapIds)
    }

    override fun reportLectureBank(lectureBankId: Int, reportId: Int): Single<CommonResponse> {
        return authApi.reportLectureBank(
            LectureBankReportRequest(
                contentId = lectureBankId,
                reportId = reportId
            )
        )
    }

    override fun reportLectureBankComment(commentId: Int, reportId: Int): Single<CommonResponse> {
        return authApi.reportLectureBankComment(
            LectureBankReportRequest(
                contentId = commentId,
                reportId = reportId
            )
        )
    }
}