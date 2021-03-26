package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.constant.MAIN_TIMETABLE
import `in`.hangang.hangang.constant.TIMETABLE_LECTURE_DIPS
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.core.Single

class TimeTableLocalDataSource : TimeTableDataSource {
    override fun getTimeTables(): Single<List<TimeTable>> {
        return Single.never()
    }

    override fun getLectureTimetableList(classification: List<String>?, department: String?, keyword: String?, limit: Int, page: Int, semesterDateId: Int): Single<List<LectureTimeTable>> {
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

    override fun setMainTimeTable(timetableId: Int): Single<Int> {
        return Single.create { subscriber ->
            try {
                Hawk.put(MAIN_TIMETABLE, timetableId)
                subscriber.onSuccess(timetableId)
            } catch (t: Throwable) {
                subscriber.onError(t)
            }
        }
        //return Single.never()
    }

    override fun getMainTimeTable(): Single<Int> {
        return Single.create { subscriber ->
            try {
                subscriber.onSuccess(Hawk.get(MAIN_TIMETABLE))
            } catch (t: Throwable) {
                subscriber.onError(t)
            }
        }
        //return Single.never()
    }

    override fun getLectureList(timetableId: Int): Single<List<LectureTimeTable>> {
        return Single.never()
    }

    override fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun removeLectureInTimeTable(
            lectureId: Int,
            timetableId: Int
    ): Single<CommonResponse> {
        return Single.never()
    }

    override fun addDipLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return Single.create { subscriber ->
            try {
                val dips = getDips()
                dips.add(lectureTimeTable)
                Hawk.put(TIMETABLE_LECTURE_DIPS, dips)

                subscriber.onSuccess(lectureTimeTable)

            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    override fun removeDipLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return Single.create { subscriber ->
            try {
                val dips = getDips()
                dips.remove(lectureTimeTable)
                Hawk.put(TIMETABLE_LECTURE_DIPS, dips)

                subscriber.onSuccess(lectureTimeTable)

            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    override fun getDipLectures(classification: List<String>?, department: String?, keyword: String?): Single<Set<LectureTimeTable>> {
        return Single.create { subscriber ->
            try {
                val dips = getDips()
                subscriber.onSuccess(dips.filter {
                    if(classification == null || classification.isEmpty()) true
                    else classification.contains(it.classification)
                }.filter {
                    if(department != null) department == it.department
                    else true
                }.filter {
                    if(keyword != null)
                        it.contains(keyword)
                    else true
                }.toSet())
            } catch (e: Exception) {
                subscriber.onError(e)
            }

        }
    }

    private fun getDips(): MutableSet<LectureTimeTable> {
        return if (Hawk.contains(TIMETABLE_LECTURE_DIPS))
            Hawk.get(TIMETABLE_LECTURE_DIPS)
        else
            mutableSetOf()
    }
}