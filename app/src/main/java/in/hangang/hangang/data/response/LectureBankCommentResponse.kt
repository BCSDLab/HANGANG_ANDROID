package `in`.hangang.hangang.data.response

import `in`.hangang.hangang.data.lecturebank.LectureBankComment

data class LectureBankCommentResponse (
    val comments : List<LectureBankComment>,
    val count : Int
)