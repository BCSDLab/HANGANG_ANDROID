package `in`.hangang.hangang.data.source.repository

import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.data.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureBankDataSource
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
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

    override fun getLectureBankComments(lectureBankId: Int): Flowable<PagingData<LectureBankComment>> {
        return lectureBankRemoteDataSource.getLectureBankComments(lectureBankId)
    }

    override fun getLectureBankDetail(id: Int): Single<LectureBankDetail> {
        return lectureBankRemoteDataSource.getLectureBankDetail(id)
    }

    override fun hitLectureBank(id: Int): Single<CommonResponse> {
        return lectureBankRemoteDataSource.hitLectureBank(id)
    }

    override fun purchaseLectureBank(id: Int): Single<CommonResponse> {
        return lectureBankRemoteDataSource.purchaseLectureBank(id)
    }

    override fun checkLectureBankPurchased(id: Int): Single<Boolean> {
        return lectureBankRemoteDataSource.checkLectureBankPurchased(id)
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

}