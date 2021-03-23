package `in`.hangang.hangang.data.source.source

import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.CommonResponse
import io.reactivex.rxjava3.core.Single

interface TimeTableDataSource {
    fun getTimeTables(): Single<List<TimeTable>>
    fun getLectureTimetableList(
            classification: List<String>? = null,
            department: String? = null,
            keyword: String? = null,
            limit: Int = 10,
            page: Int = 1,
            semesterDateId: Int
    ): Single<List<LectureTimeTable>>
    fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse>
    fun removeTimeTable(timetableId: Int): Single<CommonResponse>
    fun modifyTimeTableName(timetableId: Int, name: String): Single<CommonResponse>
    fun setMainTimeTable(timetableId: Int): Single<Int>
    fun getMainTimeTable(): Single<Int>
    fun getLectureList(timetableId: Int): Single<List<LectureTimeTable>>
    fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse>
    fun removeLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse>
}