package `in`.hangang.hangang.data.source.remote

import `in`.hangang.core.http.request.ProgressFileRequestBody
import `in`.hangang.core.http.request.ProgressRequestBodyCallback
import `in`.hangang.core.http.response.ResponseWithProgress
import `in`.hangang.core.util.getDisplayName
import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.request.LectureBankReportRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureBankDataSource
import `in`.hangang.hangang.data.source.paging.BaseRxPagingSource.Companion.DEFAULT_LIMIT
import `in`.hangang.hangang.data.source.paging.LectureBankCommentPagingSource
import `in`.hangang.hangang.data.source.paging.LectureBankPagingSource
import `in`.hangang.hangang.util.withThread
import android.content.Context
import android.net.Uri
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
import java.io.InputStream

class LectureBankRemoteDataSource(
    private val context: Context,
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

    override fun uploadSingleFile(uri: Uri): Observable<ResponseWithProgress<String>> {
        return Observable.create<ResponseWithProgress<String>?> { emitter ->
            try {
                val file = File.createTempFile(uri.getDisplayName(context) ?: uri.toString(), ".tmp").apply {
                    deleteOnExit()
                }
                val mimeType = context.contentResolver.getType(uri)

                context.contentResolver.openInputStream(uri)?.let {
                    copyToFile(it, file)
                } ?: emitter.onError(NullPointerException())

                val progressFileRequestBody =
                    ProgressFileRequestBody(context, uri, mimeType, object : ProgressRequestBodyCallback() {
                        override fun onProgress(percentage: Int) {
                            if (!emitter.isDisposed)
                                emitter.onNext(
                                    ResponseWithProgress(percentage, null)
                                )
                        }

                        override fun onError(t: Throwable) {
                            emitter.onError(t)
                        }
                    })

                val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "files",
                        uri.getDisplayName(context) ?: FILE_MULTIPART_NAME,
                        progressFileRequestBody
                    )

                val response = authApi.uploadFile(builder.build()).execute()

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

    private fun copyToFile(inputStream: InputStream, dest : File) {
        try {
            val fileOutputStream = dest.outputStream()
            var read: Int
            val bytes = ByteArray(1024)

            while (inputStream.read(bytes).also { read = it } != -1) {
                fileOutputStream.write(bytes, 0, read)
            }
        } catch (e: Exception) {

        }
    }

    companion object {
        const val FILE_MULTIPART_NAME = "file"
    }
}