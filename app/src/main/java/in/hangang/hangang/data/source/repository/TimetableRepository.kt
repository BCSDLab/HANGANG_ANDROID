package `in`.hangang.hangang.data.source.repository

import `in`.hangang.hangang.data.source.TimetableDataSource

class TimetableRepository(
    private val timetableLocalDataSource: TimetableDataSource,
    private val timetableRemoteDataSource: TimetableDataSource
) : TimetableDataSource {
    override fun getTimetable() {
        TODO("Not yet implemented")
    }
}