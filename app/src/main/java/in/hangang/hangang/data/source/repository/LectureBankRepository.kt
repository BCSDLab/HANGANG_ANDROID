package `in`.hangang.hangang.data.source.repository

import `in`.hangang.core.http.response.ResponseWithProgress
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureBankDataSource
import android.net.Uri
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class LectureBankRepository(
    private val lectureBankLocalDataSource: LectureBankDataSource,
    private val lectureBankRemoteDataSource: LectureBankDataSource
) : LectureBankDataSource {
    override fun getLectureBanks(
        categories: List<String>?,
        department: String?,
        keyword: String?,
        order: String
    ): Flowable<PagingData<LectureBank>> {
        return lectureBankRemoteDataSource.getLectureBanks(
            categories, department, keyword, order
        )
    }

    override fun uploadLectureBank(
        title: String,
        content: String,
        category: String,
        files: List<String>,
        lectureId: Int,
        semesterId: Int
    ): Single<CommonResponse> {
        return lectureBankRemoteDataSource.uploadLectureBank(title, content, category, files, lectureId, semesterId)
    }

    override fun getLectureBankComments(lectureBankId: Int): Flowable<PagingData<LectureBankComment>> {
        return lectureBankRemoteDataSource.getLectureBankComments(lectureBankId)
    }

    override fun commentLectureBank(lectureBankId: Int, comment: String): Single<Int> {
        return lectureBankRemoteDataSource.commentLectureBank(lectureBankId, comment)
    }

    override fun modifyLectureBankComment(lectureBankId: Int, commentId: Int, comment: String): Single<CommonResponse> {
        return lectureBankRemoteDataSource.modifyLectureBankComment(lectureBankId, commentId, comment)
    }

    override fun deleteLectureBankComment(lectureBankId: Int, commentId: Int): Single<CommonResponse> {
        return lectureBankRemoteDataSource.deleteLectureBankComment(lectureBankId, commentId)
    }

    override fun getLectureBankDetail(id: Int): Single<LectureBankDetail> {
        return lectureBankRemoteDataSource.getLectureBankDetail(id)
    }

    override fun toggleHitLectureBank(lectureBankId: Int): Single<CommonResponse> {
        return lectureBankRemoteDataSource.toggleHitLectureBank(lectureBankId)
    }

    override fun purchaseLectureBank(lectureBankId: Int): Single<CommonResponse> {
        return lectureBankRemoteDataSource.purchaseLectureBank(lectureBankId)
    }

    override fun checkLectureBankPurchased(lectureBankId: Int): Single<Boolean> {
        return lectureBankRemoteDataSource.checkLectureBankPurchased(lectureBankId)
    }

    override fun scrapLectureBank(lectureBankId: Int): Single<Int> {
        return lectureBankRemoteDataSource.scrapLectureBank(lectureBankId)
    }

    override fun unscrapLectureBank(scrapId: Int): Single<CommonResponse> {
        return lectureBankRemoteDataSource.unscrapLectureBank(scrapId)
    }

    override fun unscrapLectureBanks(scrapIds: List<Int>): Single<CommonResponse> {
        return lectureBankRemoteDataSource.unscrapLectureBanks(scrapIds)
    }

    override fun reportLectureBank(lectureBankId: Int, reportId: Int): Single<CommonResponse> {
        return lectureBankRemoteDataSource.reportLectureBank(lectureBankId, reportId)
    }

    override fun reportLectureBankComment(commentId: Int, reportId: Int): Single<CommonResponse> {
        return lectureBankRemoteDataSource.reportLectureBankComment(commentId, reportId)
    }

    override fun downloadSingleFile(uploadFileId: Int): Single<String> {
        return lectureBankRemoteDataSource.downloadSingleFile(uploadFileId)
    }

    override fun uploadSingleFile(uri: Uri): Observable<ResponseWithProgress<String>> {
        return lectureBankRemoteDataSource.uploadSingleFile(uri)
    }

}