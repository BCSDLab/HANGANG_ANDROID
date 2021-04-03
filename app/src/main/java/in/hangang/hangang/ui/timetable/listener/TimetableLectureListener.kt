package `in`.hangang.hangang.ui.timetable.listener

import `in`.hangang.hangang.data.entity.LectureTimeTable

interface TimetableLectureListener {
    fun onCheckedChange(position: Int, lectureTimeTable: LectureTimeTable)

    //Add 및 Remove 동작이 의도한 대로 되었다고 생각한다 -> true 리턴
    fun onAddButtonClicked(position: Int, lectureTimeTable: LectureTimeTable): Boolean
    fun onRemoveButtonClicked(position: Int, lectureTimeTable: LectureTimeTable): Boolean
    fun onReviewButtonClicked(position: Int, lectureTimeTable: LectureTimeTable)
    fun onScrapButtonClicked(position: Int, lectureTimeTable: LectureTimeTable)
}