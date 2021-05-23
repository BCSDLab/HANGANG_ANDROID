package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.data.source.LectureBankDataSource
import `in`.hangang.hangang.data.source.paging.LectureBankPagingSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable

class LectureBankRemoteDataSource(
    private val authApi: AuthApi
) : LectureBankDataSource {
    override fun getLectureBanks(
        categories: List<String>?,
        department: String?,
        keyword: String?,
        order: String
    ): Flowable<PagingData<LectureBank>> {
        return Pager(PagingConfig(pageSize = 20)) {
            LectureBankPagingSource(authApi, categories, department, keyword, order)
        }.flowable
    }
}