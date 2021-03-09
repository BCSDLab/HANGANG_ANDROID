package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.TimetableItem

interface TimetableDataSource {
    fun getTimetable() : List<TimetableItem>
    fun addTimeTable(timetableItem: TimetableItem)
    fun removeTimetable(timetableItem: TimetableItem)
}