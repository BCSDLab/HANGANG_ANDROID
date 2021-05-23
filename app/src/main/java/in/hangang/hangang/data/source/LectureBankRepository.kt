package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.lecturebank.LectureBank
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable

class LectureBankRepository(
    private val lectureBankLocalDataSource: LectureBankDataSource,
    private val lectureBankRemoteDataSource: LectureBankDataSource
) : LectureBankDataSource{
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

}