package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import io.reactivex.rxjava3.core.Single

interface TimeTableDataSource {
    fun getTimeTables(): Single<Map<Int, List<TimeTable>>>
    fun getLectureTimetableList(
        classification: List<String>? = null,
        criteria: String? = null,
        department: String? = null,
        keyword: String? = null,
        limit: Int = 10,
        page: Int = 1,
        semesterDateId: Int
    ): Single<List<`in`.hangang.hangang.data.entity.LectureTimeTable>>

    fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse>
    fun removeTimeTable(timetableId: Int): Single<CommonResponse>
    fun modifyTimeTableName(timetableId: Int, name: String): Single<CommonResponse>
    fun setMainTimeTable(timetableId: Int): Single<CommonResponse>
    fun getMainTimeTable(): Single<`in`.hangang.hangang.data.entity.TimeTableWithLecture>
    fun getTimetable(timetableId: Int): Single<`in`.hangang.hangang.data.entity.TimeTableWithLecture>
    fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse>
    fun removeLectureFromTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse>
    fun scrapLecture(lectureTimeTable: `in`.hangang.hangang.data.entity.LectureTimeTable): Single<`in`.hangang.hangang.data.entity.LectureTimeTable>
    fun unscrapLecture(lectureTimeTable: `in`.hangang.hangang.data.entity.LectureTimeTable): Single<`in`.hangang.hangang.data.entity.LectureTimeTable>
    fun getScrapLectures(
        classification: List<String>? = null,
        department: String? = null,
        keyword: String? = null
    ): Single<Collection<`in`.hangang.hangang.data.entity.LectureTimeTable>>

    fun addCustomLectureInTimetable(
        classTime: String?,
        name: String?,
        professor: String?,
        userTimetableId: Int
    ): Single<CommonResponse>

    fun getMemo(
        timetableLectureId: Int
    ): Single<`in`.hangang.hangang.data.entity.TimetableMemo>

    fun addMemo(
        timetableLectureId: Int,
        memo: String
    ): Single<CommonResponse>

    fun modifyMemo(
        timetableLectureId: Int,
        memo: String
    ): Single<CommonResponse>

    fun removeMemo(
        timetableLectureId: Int
    ): Single<CommonResponse>
}