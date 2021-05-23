package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.data.source.LectureBankDataSource
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
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
}