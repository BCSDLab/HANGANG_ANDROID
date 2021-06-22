package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.constant.TIMETABLE_DEFAULT_SEMESTER_ID
import `in`.hangang.hangang.constant.TIMETABLE_DEFAULT_TIMETABLE_NAME
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.entity.TimeTableWithLecture
import `in`.hangang.hangang.data.entity.TimetableMemo
import `in`.hangang.hangang.data.request.TimeTableCustomLectureRequest
import `in`.hangang.hangang.data.request.TimeTableRequest
import `in`.hangang.hangang.data.request.TimetableMemoRequest
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import io.reactivex.rxjava3.core.Single

class TimeTableRemoteDataSource(
        private val authApi: AuthApi
) : TimeTableDataSource {
    override fun getTimeTables(): Single<Map<Int, List<TimeTable>>> {
        return getTimetablesOrMakeNew()
            .map { list -> list.groupBy { it.semesterDateId } }
    }

    override fun getLectureTimetableList(
            classification: List<String>?,
            criteria: String?,
            department: String?,
            keyword: String?,
            limit: Int,
            page: Int,
            semesterDateId: Int
    ): Single<List<LectureTimeTable>> {
        return authApi.getTimetableLectureList(
                classification, criteria, department, keyword, limit, page, semesterDateId
        ).map { it.map { item -> item.copy(lectureId = item.id) }.toList() }
    }

    override fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse> {
        return authApi.makeTimeTable(userTimeTableRequest)
    }

    override fun removeTimeTable(timetableId: Int): Single<CommonResponse> {
        return authApi.deleteTimeTable(TimeTableRequest(id = timetableId))
    }

    override fun modifyTimeTableName(timetableId: Int, name: String): Single<CommonResponse> {
        return authApi.modifyTimeTableName(
                UserTimeTableRequest(
                        id = timetableId,
                        name = name
                )
        )
    }

    override fun setMainTimeTable(timetableId: Int): Single<CommonResponse> {
        return authApi.setMainTimeTable(
            TimeTableRequest(id = timetableId)
        )
    }

    override fun getMainTimeTable(): Single<TimeTableWithLecture> {
        var attempts = 0
        return authApi.getMainTimeTable()
            .onErrorResumeNext {
                getTimetablesOrMakeNew().flatMap {
                    authApi.setMainTimeTable(
                        TimeTableRequest(
                            id = it[0].id
                        )
                    )
                }.flatMap {
                    authApi.getMainTimeTable()
                }
            }
    }

    override fun getTimetable(timetableId: Int): Single<TimeTableWithLecture> {
        return authApi.getLectureListFromTimeTable(timetableId)
    }

    override fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse> {
        return authApi.addLectureInTimeTable(
                TimeTableRequest(lectureId, timetableId)
        )
    }

    override fun removeLectureFromTimeTable(
            lectureId: Int,
            timetableId: Int
    ): Single<CommonResponse> {
        return authApi.removeLectureInTimeTable(
                TimeTableRequest(lectureId, timetableId)
        )
    }

    override fun scrapLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return authApi.scrapLecture(
                TimeTableRequest(
                        lectureId = lectureTimeTable.lectureId
                )
        )
                .flatMap { Single.just(lectureTimeTable) }
    }

    override fun unscrapLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return authApi.unscrapLecture(
                TimeTableRequest(
                        lectureId = lectureTimeTable.lectureId
                )
        )
                .flatMap { Single.just(lectureTimeTable) }
    }

    override fun getScrapLectures(
            classification: List<String>?,
            department: String?,
            keyword: String?
    ): Single<Collection<LectureTimeTable>> {
        return authApi.getScrapLectures()
                .flatMap { list ->
                    val collection: Collection<LectureTimeTable> = list.filter {
                        if (classification == null || classification.isEmpty()) true
                        else classification.contains(it.classification)
                    }.filter {
                        if (department != null) department == it.department
                        else true
                    }.filter {
                        if (keyword != null)
                            it.contains(keyword)
                        else true
                    }.map {
                        it.copy(lectureId = it.id)
                    }.toSet()

                    Single.just(collection)
                }
    }

    override fun addCustomLectureInTimetable(
            classTime: String?,
            name: String?,
            professor: String?,
            userTimetableId: Int
    ): Single<CommonResponse> {
        return authApi.addCustomLectureInTimetable(
                TimeTableCustomLectureRequest(
                        classTime, name, professor, userTimetableId
                )
        )
    }

    override fun getMemo(timetableLectureId: Int): Single<TimetableMemo> {
        return authApi.getTimetableMemo(timetableLectureId)
    }

    override fun addMemo(timetableLectureId: Int, memo: String): Single<CommonResponse> {
        return authApi.addTimetableMemo(
                TimetableMemoRequest(
                        timetableLectureId, memo
                )
        )
    }

    override fun modifyMemo(timetableLectureId: Int, memo: String): Single<CommonResponse> {
        return authApi.modifyTimetableMemo(
                TimetableMemoRequest(
                        timetableLectureId, memo
                )
        )
    }

    override fun removeMemo(timetableLectureId: Int): Single<CommonResponse> {
        return authApi.removeTimetableMemo(
                TimetableMemoRequest(
                        timetableLectureId = timetableLectureId,
                        memo = null
                )
        )
    }

    private fun getTimetablesOrMakeNew(): Single<List<TimeTable>> {
        return authApi.getTimeTables()
            .flatMap { list ->
                if (list.isEmpty()) {
                    makeTimeTable(
                        UserTimeTableRequest(
                            name = TIMETABLE_DEFAULT_TIMETABLE_NAME,
                            semesterDateId = TIMETABLE_DEFAULT_SEMESTER_ID
                        )
                    ).flatMap {
                        authApi.getTimeTables()
                    }
                } else
                    Single.just(list)
            }
    }

    override fun getUserTimeTables(semesterId: Long?): Single<List<TimeTable>> {
        return authApi.getTimeTables(semesterId)
    }

    override suspend fun fetchLectureListFromTimeTable(timetableId: Int): TimeTableWithLecture {
        return authApi.fetchLectureListFromTimeTable(timetableId)
    }

    override suspend fun fetchTimeTables(semesterDateId: Long?): List<TimeTable> {
        return authApi.fetchTimeTables(semesterDateId)
    }
}