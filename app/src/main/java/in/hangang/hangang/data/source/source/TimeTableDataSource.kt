package `in`.hangang.hangang.data.source.source

import `in`.hangang.hangang.data.entity.LectureTimeTable

interface TimeTableDataSource {
    fun getTimetable() : List<LectureTimeTable>
    fun addTimeTable(timetableItem: LectureTimeTable)
    fun removeTimetable(timetableItem: LectureTimeTable)
}