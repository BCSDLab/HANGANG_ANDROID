package `in`.hangang.hangang.data.source.remote

import `in`.hangang.core.http.request.ProgressFileRequestBody
import `in`.hangang.core.http.request.ProgressRequestBodyCallback
import `in`.hangang.core.http.response.ResponseWithProgress
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
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.withThread
import androidx.compose.runtime.emit
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.File
import java.lang.Exception

class LectureBankRemoteDataSource(
    private val authApi: AuthApi
) : LectureBankDataSource {

    private var currentLectureBankCommentPagingSource: LectureBankCommentPagingSource? = null

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
            LectureBankCommentPagingSource(authApi, lectureBankId).also {
                currentLectureBankCommentPagingSource = it
            }
        }.flowable
    }

    override fun commentLectureBank(lectureBankId: Int, comment: String): Single<Int> {
        return authApi.commentLectureBank(lectureBankId, LectureBankComment(comments = comment))
    }

    override fun modifyLectureBankComment(lectureBankId: Int, commentId: Int, comment: String): Single<CommonResponse> {
        return authApi.modifyLectureBankComment(lectureBankId, commentId, LectureBankComment(comments = comment))
    }

    override fun deleteLectureBankComment(lectureBankId: Int, commentId: Int): Single<CommonResponse> {
        return authApi.deleteLectureBankComment(lectureBankId, commentId)
    }

    override fun getLectureBankDetail(id: Int): Single<LectureBankDetail> {
        return authApi.getLectureBankDetail(id)
    }

    override fun hitLectureBank(lectureBankId: Int): Single<CommonResponse> {
        return authApi.hitLectureBank(lectureBankId)
    }

    override fun purchaseLectureBank(lectureBankId: Int): Single<CommonResponse> {
        return authApi.purchaseLectureBank(lectureBankId)
    }

    override fun checkLectureBankPurchased(lectureBankId: Int): Single<Boolean> {
        return authApi.checkLectureBankPurchased(lectureBankId)
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

    override fun downloadSingleFile(uploadFileId: Int): Single<String> {
        return authApi.downloadSingleFile(uploadFileId)
    }

    override fun uploadSingleFile(file: File, contentType: String): Observable<ResponseWithProgress<String>> {
        return Observable.create<ResponseWithProgress<String>?> { emitter ->
            try {
                val progressFileRequestBody =
                    ProgressFileRequestBody(file, contentType, object : ProgressRequestBodyCallback() {
                        override fun onProgress(percentage: Int) {
                            if (emitter.isDisposed)
                                emitter.onNext(
                                    ResponseWithProgress(percentage, null)
                                )
                        }

                        override fun onError(t: Throwable) {
                            emitter.onError(t)
                        }
                    })
                val filePart =
                    MultipartBody.Part.createFormData(FILE_MULTIPART_NAME, file.name, progressFileRequestBody)
                val response = authApi.uploadFile(filePart).execute()
                if (emitter.isDisposed) return@create
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    emitter.onNext(
                        ResponseWithProgress(100, response.body()!![0])
                    )
                    emitter.onComplete()
                } else {
                    emitter.onError(HttpException(response))
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.withThread()
    }

    companion object {
        const val FILE_MULTIPART_NAME = "file"
    }
}