package `in`.hangang.hangang.data.source.local

import `in`.hangang.core.http.response.ResponseWithProgress
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankScrap
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureBankDataSource
import android.net.Uri
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class LectureBankLocalDataSource : LectureBankDataSource {

    override fun getLectureBanks(
        categories: List<String>?,
        department: String?,
        keyword: String?,
        order: String
    ): Flowable<PagingData<LectureBank>> {
        return Flowable.never()
    }

    override fun uploadLectureBank(
        title: String,
        content: String,
        category: String,
        files: List<String>,
        lectureId: Int,
        semesterId: Int
    ): Single<CommonResponse> {
        return Single.never()
    }

    override fun getLectureBankComments(lectureBankId: Int): Flowable<PagingData<LectureBankComment>> {
        return Flowable.never()
    }

    override fun commentLectureBank(lectureBankId: Int, comment: String): Single<Int> {
        return Single.never()
    }

    override fun modifyLectureBankComment(lectureBankId: Int, commentId: Int, comment: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun deleteLectureBankComment(lectureBankId: Int, commentId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun getLectureBankDetail(id: Int): Single<LectureBankDetail> {
        return Single.never()
    }

    override fun toggleHitLectureBank(lectureBankId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun purchaseLectureBank(lectureBankId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun checkLectureBankPurchased(lectureBankId: Int): Single<Boolean> {
        return Single.never()
    }

    override fun scrapLectureBank(lectureBankId: Int): Single<Int> {
        return Single.never()
    }

    override fun unscrapLectureBank(scrapId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun unscrapLectureBanks(scrapIds: List<Int>): Single<CommonResponse> {
        return Single.never()
    }

    override fun reportLectureBank(lectureBankId: Int, reportId: Int) : Single<CommonResponse>{
        return Single.never()
    }

    override fun reportLectureBankComment(commentId: Int, reportId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun downloadSingleFile(uploadFileId: Int): Single<String> {
        return Single.never()
    }

    override fun uploadSingleFile(uri: Uri): Observable<ResponseWithProgress<String>> {
        return Observable.never()
    }
    override fun getScrapLectureBanks(): Single<List<LectureBankScrap>> {
        return Single.never()
    }
}