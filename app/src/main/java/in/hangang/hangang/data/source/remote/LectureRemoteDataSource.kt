package `in`.hangang.hangang.data.source.remote

import `in`.hangang.hangang.api.AuthApi
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.request.ScrapLectureRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureDataSource
import io.reactivex.rxjava3.core.Single

class LectureRemoteDataSource(
    private val authApi: AuthApi
) : LectureDataSource {
    override fun scrapLecture(lectureId: Int): Single<CommonResponse> {
        return authApi.addScrapLecture(
            ScrapLectureRequest(lectureId)
        )
    }

    override fun unscrapLecture(lectureId: Int): Single<CommonResponse> {
        return authApi.removeScrapLecture(
            ScrapLectureRequest(lectureId)
        )
    }

    override fun getScrapedLecture(): Single<List<Lecture>> {
        return authApi.getScrapedLecture()
    }
}