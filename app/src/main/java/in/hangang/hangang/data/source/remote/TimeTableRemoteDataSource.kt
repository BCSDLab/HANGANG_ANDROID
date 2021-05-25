package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.constant.TIMETABLE_DEFAULT_SEMESTER_ID
import `in`.hangang.hangang.constant.TIMETABLE_DEFAULT_TIMETABLE_NAME
import `in`.hangang.hangang.data.evaluation.LectureTimeTable
import `in`.hangang.hangang.data.evaluation.TimeTable
import `in`.hangang.hangang.data.evaluation.TimeTableWithLecture
import `in`.hangang.hangang.data.evaluation.TimetableMemo
import `in`.hangang.hangang.data.request.TimeTableRequest
import `in`.hangang.hangang.data.request.UserTimeTableRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.TimeTableDataSource
import io.reactivex.rxjava3.core.Single

class TimeTableRemoteDataSource(
    private val authApi: AuthApi
) : TimeTableDataSource {
    override fun getTimeTables(): Single<Map<Int, List<TimeTable>>> {
        return getTimetablesOrMakeNew()
            .map { list -> list.groupBy { it.semesterDateId } }
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
}