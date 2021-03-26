package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.data.request.TimeTableRequest
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.api.NoAuthApi
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.source.TimeTableDataSource
import `in`.hangang.hangang.util.SemesterUtil
import io.reactivex.rxjava3.core.Single

class TimeTableRemoteDataSource(
    private val noAuthApi: NoAuthApi,
    private val authApi: AuthApi,
    private val refreshApi: AuthApi
) : TimeTableDataSource {
    override fun getTimeTables(): Single<List<TimeTable>> {
        return Single.create { subscriber ->
            authApi.getTimeTables().subscribe({ list1 ->
                if(list1.isEmpty()) {
                    makeTimeTable(
                        UserTimeTableRequest(
                            name = "2021년 1학기 (1)",
                            semesterDateId = SemesterUtil.currentSemester
                        )
                    ).subscribe({
                        authApi.getTimeTables().subscribe({ list2 ->
                            subscriber.onSuccess(list2)
                        }, {
                            subscriber.onError(it)
                        })
                    }, {
                        subscriber.onError(it)
                    })
                } else
                    subscriber.onSuccess(list1)
            }, {
                subscriber.onError(it)
            })
        }
    }

    override fun getLectureTimetableList(classification: List<String>?, department: String?, keyword: String?, limit: Int, page: Int, semesterDateId: Int): Single<List<LectureTimeTable>> {
        return noAuthApi.getTimetableLectureList(
                classification, department, keyword, limit, page, semesterDateId
        )
    }

    override fun makeTimeTable(userTimeTableRequest: UserTimeTableRequest): Single<CommonResponse> {
        return authApi.makeTimeTable(userTimeTableRequest)
    }

    override fun removeTimeTable(timetableId: Int): Single<CommonResponse> {
        return authApi.deleteTimeTable(TimeTableRequest(userTimeTableId = timetableId))
    }

    override fun modifyTimeTableName(timetableId: Int, name: String): Single<CommonResponse> {
        return authApi.modifyTimeTableName(
            UserTimeTableRequest(
                id = timetableId,
                name = name
            )
        )
    }

    override fun setMainTimeTable(timetableId: Int): Single<Int> {
        return Single.never()
    }

    override fun getMainTimeTable(): Single<Int> {
        return Single.never()
    }

    override fun getLectureList(timetableId: Int): Single<List<LectureTimeTable>> {
        return authApi.getLectureListFromTimeTable(timetableId)
    }

    override fun addLectureInTimeTable(lectureId: Int, timetableId: Int): Single<CommonResponse> {
        return authApi.addLectureInTimeTable(
            TimeTableRequest(lectureId, timetableId)
        )
    }

    override fun removeLectureInTimeTable(
        lectureId: Int,
        timetableId: Int
    ): Single<CommonResponse> {
        return authApi.removeLectureInTimeTable(
            TimeTableRequest(lectureId, timetableId)
        )
    }

    override fun addDipLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return Single.never()
    }

    override fun removeDipLecture(lectureTimeTable: LectureTimeTable): Single<LectureTimeTable> {
        return Single.never()
    }

    override fun getDipLectures(classification: List<String>?, department: String?, keyword: String?): Single<Set<LectureTimeTable>> {
        return Single.never()
    }
}