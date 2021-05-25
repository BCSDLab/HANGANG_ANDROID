package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.evaluation.LectureTimeTable
import `in`.hangang.hangang.data.evaluation.TimeTable
import `in`.hangang.hangang.data.evaluation.TimeTableWithLecture
import `in`.hangang.hangang.data.evaluation.TimetableMemo
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import io.reactivex.rxjava3.core.Single

interface TimeTableDataSource {
    fun getTimeTables(): Single<Map<Int, List<TimeTable>>>
    fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse>
    fun removeTimeTable(timetableId: Int): Single<CommonResponse>
    fun modifyTimeTableName(timetableId: Int, name: String): Single<CommonResponse>
    fun getTimetable(timetableId: Int): Single<TimeTableWithLecture>
    fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse>
    fun removeLectureFromTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse>
}