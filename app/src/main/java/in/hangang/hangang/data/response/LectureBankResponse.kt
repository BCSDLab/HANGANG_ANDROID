package `in`.hangang.hangang.data.response

import `in`.hangang.hangang.data.entity.lecturebank.LectureBank

data class LectureBankResponse(
    val result : List<LectureBank>,
    val count : Int
)
