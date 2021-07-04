package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.timetable.LectureTimeTable
import `in`.hangang.hangang.data.entity.timetable.TimeTable
import `in`.hangang.hangang.data.entity.timetable.TimeTableWithLecture
import `in`.hangang.hangang.data.entity.timetable.TimetableMemo
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
    ): Single<List<LectureTimeTable>>

    fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse>
    fun removeTimeTable(timetableId: Int): Single<CommonResponse>
    fun modifyTimeTableName(timetableId: Int, name: String): Single<CommonResponse>
    fun setMainTimeTable(timetableId: Int): Single<CommonResponse>
    fun getMainTimeTable(): Single<TimeTableWithLecture>
    fun getTimetable(timetableId: Int): Single<TimeTableWithLecture>
    fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<LectureTimeTable>
    fun removeLectureFromTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse>
    fun scrapLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable>
    fun unscrapLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable>
    fun getScrapLectures(
            classification: List<String>? = null,
            department: String? = null,
            keyword: String? = null
    ): Single<Collection<LectureTimeTable>>

    fun addCustomLectureInTimetable(
            classTime: String?,
            name: String?,
            professor: String?,
            userTimetableId: Int
    ): Single<LectureTimeTable>

    fun getMemo(
            timetableLectureId: Int
    ): Single<TimetableMemo>

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
    fun getUserTimeTables(semesterId: Long?): Single<List<TimeTable>>

    suspend fun fetchLectureListFromTimeTable(timetableId: Int): TimeTableWithLecture
    suspend fun fetchTimeTables(semesterDateId: Long? = null): List<TimeTable>
}