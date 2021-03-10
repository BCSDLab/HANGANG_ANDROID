package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.data.entity.TimetableItem
import `in`.hangang.hangang.data.source.TimetableDataSource

class TimetableRemoteDataSource(
    private val noAuthApi: NoAuthApi,
    private val authApi: AuthApi,
    private val refreshApi: AuthApi
) : TimetableDataSource{
    override fun getTimetable(): List<TimetableItem> {
        TODO("Not yet implemented")
    }

    override fun addTimeTable(timetableItem: TimetableItem) {
        TODO("Not yet implemented")
    }

    override fun removeTimetable(timetableItem: TimetableItem) {
        TODO("Not yet implemented")
    }
}