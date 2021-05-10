package `in`.hangang.hangang.data.source.local

import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureDataSource
import io.reactivex.rxjava3.core.Single

class LectureLocalDataSource : LectureDataSource {
    override fun scrapLecture(lectureId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun unscrapLecture(vararg lectureId: Int): Single<CommonResponse> {
        return Single.never()
    }

    override fun getScrapedLecture(): Single<List<Lecture>> {
        return Single.never()
    }
}