package `in`.hangang.hangang.data.source

import `in`.hangang.core.http.response.ResponseWithProgress
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.response.CommonResponse
import android.net.Uri
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LectureBankDataSource {
    fun getLectureBanks(
        categories: List<String>?,
        department: String?,
        keyword: String?,
        order: String
    ) : Flowable<PagingData<LectureBank>>

    fun uploadLectureBank(
        title: String,
        content: String,
        category: String,
        files: List<String>,
        lectureId: Int,
        semesterId: Int
    ) : Single<CommonResponse>

    fun getLectureBankComments(
        lectureBankId: Int
    ) : Flowable<PagingData<LectureBankComment>>
    fun commentLectureBank(
        lectureBankId: Int,
        comment: String
    ): Single<Int>
    fun modifyLectureBankComment(
        lectureBankId: Int,
        commentId: Int,
        comment: String
    ) : Single<CommonResponse>
    fun deleteLectureBankComment(
        lectureBankId: Int,
        commentId: Int
    ) : Single<CommonResponse>

    fun getLectureBankDetail(id: Int) : Single<LectureBankDetail>

    fun toggleHitLectureBank(lectureBankId: Int) : Single<CommonResponse>

    fun purchaseLectureBank(lectureBankId: Int) : Single<CommonResponse>

    fun checkLectureBankPurchased(lectureBankId: Int) : Single<Boolean>

    fun scrapLectureBank(lectureBankId: Int) : Single<Int>
    fun unscrapLectureBank(scrapId: Int) : Single<CommonResponse>
    fun unscrapLectureBanks(scrapIds: List<Int>) : Single<CommonResponse>

    fun reportLectureBank(lectureBankId: Int, reportId: Int) : Single<CommonResponse>
    fun reportLectureBankComment(commentId: Int, reportId: Int) : Single<CommonResponse>

    fun downloadSingleFile(uploadFileId: Int) : Single<String>
    fun uploadSingleFile(uri: Uri) : Observable<ResponseWithProgress<String>>
}