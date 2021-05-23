package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.lecturebank.LectureBank
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Query

interface LectureBankDataSource {
    fun getLectureBanks(
        categories: List<String>?,
        department: String?,
        keyword: String?,
        order: String
    ) : Flowable<PagingData<LectureBank>>
}