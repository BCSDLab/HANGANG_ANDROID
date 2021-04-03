package `in`.hangang.hangang.ui.timetable.listener

import `in`.hangang.hangang.data.entity.TimeTable

interface TimetableListRecyclerViewOnItemClickListener {
    fun onSemesterItemClick(semesterDateId: Int, semesterDateString: String) {}
    fun onSemesterItemLongClick(semesterDateId: Int, semesterDateString: String) {}
    fun onTimeTableItemClick(timetable: TimeTable) {}
    fun onTimeTableItemLongClick(timetable: TimeTable) {}
}