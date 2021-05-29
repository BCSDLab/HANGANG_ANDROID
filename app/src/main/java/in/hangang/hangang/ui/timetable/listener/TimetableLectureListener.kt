package `in`.hangang.hangang.ui.timetable.listener

import `in`.hangang.hangang.data.entity.LectureTimeTable

interface TimetableLectureListener {
    fun onCheckedChange(position: Int, lectureTimeTable: LectureTimeTable)
    fun onAddButtonClicked(position: Int, lectureTimeTable: LectureTimeTable): Boolean
    fun onRemoveButtonClicked(position: Int, lectureTimeTable: LectureTimeTable): Boolean
    fun onReviewButtonClicked(position: Int, lectureTimeTable: LectureTimeTable)
    fun onScrapButtonClicked(position: Int, lectureTimeTable: LectureTimeTable)
}