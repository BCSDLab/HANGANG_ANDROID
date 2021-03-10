package `in`.hangang.hangang.data.source

import `in`.hangang.hangang.data.source.source.LectureDataSource

class LectureRepository(
    private val lectureLocalDataSource: LectureDataSource,
    private val lectureRemoteDataSource: LectureDataSource
) : LectureDataSource {
    override fun getLectureRanking(major: String) {
        TODO("Not yet implemented")
    }

    override fun getRecommendedLectures() {
        TODO("Not yet implemented")
    }

    override fun getRecentLectures() {
        TODO("Not yet implemented")
    }
}