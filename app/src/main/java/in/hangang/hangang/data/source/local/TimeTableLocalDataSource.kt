package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.constant.TIMETABLE_LECTURE_DIPS
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.entity.TimetableMemo
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.core.Single

class TimeTableLocalDataSource : TimeTableDataSource {
    override fun getTimeTables(): Single<Map<Int, List<TimeTable>>> {
        return Single.never()
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
        return Single.never()
    }

    override fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse> {
        return Single.never()
    }

    override fun removeTimeTable(timetableId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun modifyTimeTableName(timetableId: Int, name: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun setMainTimeTable(timetableId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun getMainTimeTable(): Single<Int> {
        return Single.never()
    }

    override fun getLectureList(timetableId: Int): Single<List<LectureTimeTable>> {
        return Single.never()
    }

    override fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun removeLectureFromTimeTable(
        lectureId: Int,
        timetableId: Int
    ): Single<CommonResponse> {
        return Single.never()
    }

    override fun scrapLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return Single.never()
    }

    override fun unscrapLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return Single.never()
    }

    override fun getScrapLectures(
        classification: List<String>?,
        department: String?,
        keyword: String?
    ): Single<Collection<LectureTimeTable>> {
        return Single.create { subscriber ->
            try {
                val dips = getDips()
                subscriber.onSuccess(dips.filter {
                    if (classification == null || classification.isEmpty()) true
                    else classification.contains(it.classification)
                }.filter {
                    if (department != null) department == it.department
                    else true
                }.filter {
                    if (keyword != null)
                        it.contains(keyword)
                    else true
                }.toSet())
            } catch (e: Exception) {
                subscriber.onError(e)
            }

        }
    }

    override fun addCustomLectureInTimetable(
        classTime: String?,
        name: String?,
        professor: String?,
        userTimetableId: Int
    ): Single<CommonResponse> {
        return Single.never()
    }

    override fun getMemo(timetableLectureId: Int): Single<TimetableMemo> {
        return Single.never()
    }

    override fun addMemo(timetableLectureId: Int, memo: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun modifyMemo(timetableLectureId: Int, memo: String): Single<CommonResponse> {
        return Single.never()
    }

    override fun removeMemo(timetableLectureId: Int): Single<CommonResponse> {
        return Single.never()
    }

    private fun getDips(): MutableSet<LectureTimeTable> {
        return if (Hawk.contains(TIMETABLE_LECTURE_DIPS))
            Hawk.get(TIMETABLE_LECTURE_DIPS)
        else
            mutableSetOf()
    }
}