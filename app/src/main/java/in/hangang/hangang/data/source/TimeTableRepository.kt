package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.TimetableItem
import `in`.hangang.hangang.data.source.source.TimeTableDataSource

class TimeTableRepository(
    private val timeTableLocalDataSource: TimeTableDataSource,
    private val timeTableRemoteDataSource: TimeTableDataSource
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