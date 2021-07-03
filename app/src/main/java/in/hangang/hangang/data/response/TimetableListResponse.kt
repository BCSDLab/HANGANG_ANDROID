package `in`.hangang.hangang.data.response

import `in`.hangang.hangang.data.entity.timetable.LectureTimeTable

data class TimetableListResponse(
    val result : List<LectureTimeTable>,
    val count : Int
)