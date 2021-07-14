package `in`.hangang.hangang.data.response

import `in`.hangang.hangang.data.entity.timetable.LectureTimeTable

data class MainTimeTableResponse(
        val id: Int,
        val tableName: Int,
        val tableSemesterDate: Int,
        val lectureList: LectureTimeTable
)