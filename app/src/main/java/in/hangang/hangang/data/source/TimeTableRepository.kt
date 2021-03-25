package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class TimeTableRepository(
        private val timeTableLocalDataSource: TimeTableDataSource,
        private val timeTableRemoteDataSource: TimeTableDataSource
) : TimeTableDataSource {
    override fun getTimeTables(): Single<List<TimeTable>> {
        return timeTableRemoteDataSource.getTimeTables()
    }

    override fun getLectureTimetableList(classification: List<String>?, department: String?, keyword: String?, limit: Int, page: Int, semesterDateId: Int): Single<List<LectureTimeTable>> {
        return timeTableRemoteDataSource.getLectureTimetableList(classification, department, keyword, limit, page, semesterDateId)
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

    override fun setMainTimeTable(timetableId: Int): Single<Int> {
        return timeTableLocalDataSource.setMainTimeTable(timetableId)
    }

    override fun getMainTimeTable(): Single<Int> {
        return timeTableLocalDataSource.getMainTimeTable()
    }

    override fun getLectureList(timetableId: Int): Single<List<LectureTimeTable>> {
        return timeTableRemoteDataSource.getLectureList(timetableId)
    }

    override fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse> {
        return timeTableRemoteDataSource.addLectureInTimeTable(lectureId, timetableId)
    }

    override fun removeLectureInTimeTable(
        lectureId: Int,
        timetableId: Int
    ): Single<CommonResponse> {
        return timeTableRemoteDataSource.removeLectureInTimeTable(lectureId, timetableId)
    }

    override fun addDipLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return timeTableLocalDataSource.addDipLecture(lectureTimeTable)
    }

    override fun removeDipLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return timeTableLocalDataSource.removeDipLecture(lectureTimeTable)
    }

    override fun getDipLectures(): Single<Set<LectureTimeTable>> {
        return timeTableLocalDataSource.getDipLectures()
    }
}