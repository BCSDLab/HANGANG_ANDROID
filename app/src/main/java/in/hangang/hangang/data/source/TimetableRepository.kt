package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.TimetableItem

class TimetableRepository(
        private val timetableLocalDataSource: TimetableDataSource,
        private val timetableRemoteDataSource: TimetableDataSource
) : TimetableDataSource {
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