package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.response.CommonResponse
import io.reactivex.rxjava3.core.Single

interface LectureDataSource {
    fun scrapLecture(lectureId: Int): Single<CommonResponse>
    fun unscrapLecture(lectureId: Int): Single<CommonResponse>
    fun getScrapedLecture(): Single<List<Lecture>>
}