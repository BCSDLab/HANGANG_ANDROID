package `in`.hangang.hangang.data.source.repository

import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.source.LectureDataSource
import io.reactivex.rxjava3.core.Single

class LectureRepository(
    private val lectureLocalDataSource: LectureDataSource,
    private val lectureRemoteDataSource: LectureDataSource
) : LectureDataSource {
    override fun scrapLecture(lectureId: Int): Single<CommonResponse> {
        return lectureRemoteDataSource.scrapLecture(lectureId)
    }

    override fun unscrapLecture(vararg lectureId: Int): Single<CommonResponse> {
        return lectureRemoteDataSource.unscrapLecture(*lectureId)
    }

    override fun getScrapedLecture(): Single<List<Lecture>> {
        return lectureRemoteDataSource.getScrapedLecture()
    }
}