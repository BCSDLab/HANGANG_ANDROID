package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.constant.LECTURE_BANKS_PURCHASE
import `in`.hangang.hangang.constant.LECTURE_BANKS_PURCHASE_CHECK
import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.data.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.response.CommonResponse
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LectureBankDataSource {
    fun getLectureBanks(
        categories: List<String>?,
        department: String?,
        keyword: String?,
        order: String
    ) : Flowable<PagingData<LectureBank>>

    fun getLectureBankComments(
        lectureBankId: Int
    ) : Flowable<PagingData<LectureBankComment>>
    fun commentLectureBank(
        lectureBankId: Int,
        comment: String
    ): Single<Int>

    fun getLectureBankDetail(id: Int) : Single<LectureBankDetail>

    fun hitLectureBank(lectureBankId: Int) : Single<CommonResponse>

    fun purchaseLectureBank(lectureBankId: Int) : Single<CommonResponse>

    fun checkLectureBankPurchased(lectureBankId: Int) : Single<Boolean>

    fun scrapLectureBank(lectureBankId: Int) : Single<Int>
    fun unscrapLectureBank(scrapId: Int) : Single<CommonResponse>
    fun unscrapLectureBanks(scrapIds: List<Int>) : Single<CommonResponse>

    fun reportLectureBank(lectureBankId: Int, reportId: Int) : Single<CommonResponse>
    fun reportLectureBankComment(commentId: Int, reportId: Int) : Single<CommonResponse>
}