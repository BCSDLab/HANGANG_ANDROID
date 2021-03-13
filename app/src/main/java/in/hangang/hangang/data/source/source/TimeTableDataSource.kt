package `in`.hangang.hangang.data.source.source

import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.request.LecturesParameter
import `in`.hangang.hangang.data.response.TimeTableResponse
import io.reactivex.rxjava3.core.Single

interface TimeTableDataSource {
    fun getTimetables() : Single<List<TimeTableResponse>>
    fun getLectures(lecturesParameter: LecturesParameter) : Single<List<Lecture>>
}