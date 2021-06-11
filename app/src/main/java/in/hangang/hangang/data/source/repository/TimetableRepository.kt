package `in`.hangang.hangang.data.source.repository

import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.entity.TimeTableWithLecture
import `in`.hangang.hangang.data.entity.TimetableMemo
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.TimeTableDataSource
import io.reactivex.rxjava3.core.Single

class TimeTableRepository(
        private val timeTableLocalDataSource: TimeTableDataSource,
        private val timeTableRemoteDataSource: TimeTableDataSource
) : TimeTableDataSource {
    override fun getTimeTables(): Single<Map<Int, List<TimeTable>>> {
        return timeTableRemoteDataSource.getTimeTables()
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
        return timeTableRemoteDataSource.getLectureTimetableList(
                classification,
                criteria,
                department,
                keyword,
                limit,
                page,
                semesterDateId
        )
    }

    override fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse> {
        return timeTableRemoteDataSource.makeTimeTable(userTimeTableRequest)
    }

    override fun removeTimeTable(timetableId: Int): Single<CommonResponse> {
        return timeTableRemoteDataSource.removeTimeTable(timetableId)
    }

    override fun modifyTimeTableName(
            timetableId: Int,
            name: String
    ): Single<CommonResponse> {
        return timeTableRemoteDataSource.modifyTimeTableName(timetableId, name)
    }

    override fun setMainTimeTable(timetableId: Int): Single<CommonResponse> {
        return timeTableRemoteDataSource.setMainTimeTable(timetableId)
    }

    override fun getMainTimeTable(): Single<TimeTableWithLecture> {
        return timeTableRemoteDataSource.getMainTimeTable()
    }

    override fun getTimetable(timetableId: Int): Single<TimeTableWithLecture> {
        return timeTableRemoteDataSource.getTimetable(timetableId)
    }

    override fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse> {
        return timeTableRemoteDataSource.addLectureInTimeTable(lectureId, timetableId)
    }

    override fun removeLectureFromTimeTable(
            lectureId: Int,
            timetableId: Int
    ): Single<CommonResponse> {
        return timeTableRemoteDataSource.removeLectureFromTimeTable(lectureId, timetableId)
    }

    override fun scrapLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return timeTableRemoteDataSource.scrapLecture(lectureTimeTable)
    }

    override fun unscrapLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return timeTableRemoteDataSource.unscrapLecture(lectureTimeTable)
    }

    override fun getScrapLectures(
            classifications: List<String>?,
            department: String?,
            keyword: String?
    ): Single<Collection<LectureTimeTable>> {
        return timeTableRemoteDataSource.getScrapLectures(classifications, department, keyword)
    }

    override fun addCustomLectureInTimetable(
            classTime: String?,
            name: String?,
            professor: String?,
            userTimetableId: Int
    ): Single<CommonResponse> {
        return timeTableRemoteDataSource.addCustomLectureInTimetable(
                classTime, name, professor, userTimetableId
        )
    }

    override fun getMemo(timetableLectureId: Int): Single<TimetableMemo> {
        return timeTableRemoteDataSource.getMemo(timetableLectureId)
    }

    override fun addMemo(timetableLectureId: Int, memo: String): Single<CommonResponse> {
        return timeTableRemoteDataSource.addMemo(timetableLectureId, memo)
    }

    override fun modifyMemo(timetableLectureId: Int, memo: String): Single<CommonResponse> {
        return Single.create { subscriber ->
            timeTableRemoteDataSource.getMemo(timetableLectureId)
                    .subscribe({
                        timeTableRemoteDataSource.addMemo(timetableLectureId, memo)
                                .subscribe({
                                    subscriber.onSuccess(it)
                                }, {
                                    subscriber.onError(it)
                                })
                    }, { t ->
                        if (t.toCommonResponse().code == 30) {
                            timeTableRemoteDataSource.addMemo(timetableLectureId, memo)
                                    .subscribe({
                                        subscriber.onSuccess(it)
                                    }, {
                                        subscriber.onError(it)
                                    })
                        } else {
                            subscriber.onError(t)
                        }
                    })
        }
    }

    override fun removeMemo(timetableLectureId: Int): Single<CommonResponse> {
        return timeTableRemoteDataSource.removeMemo(timetableLectureId)
    }
}