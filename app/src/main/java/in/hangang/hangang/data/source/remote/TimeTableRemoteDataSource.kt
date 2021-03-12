package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.data.entity.TimetableItem
import `in`.hangang.hangang.data.source.source.TimeTableDataSource

class TimeTableRemoteDataSource(
    private val noAuthApi: NoAuthApi,
    private val authApi: AuthApi,
    private val refreshApi: AuthApi
) : TimeTableDataSource {
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