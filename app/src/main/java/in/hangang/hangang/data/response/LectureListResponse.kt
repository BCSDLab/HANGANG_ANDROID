package `in`.hangang.hangang.data.response

import `in`.hangang.hangang.data.entity.timetable.Lecture

data class LectureListResponse(
    val result : List<Lecture>,
    val count : Int
)