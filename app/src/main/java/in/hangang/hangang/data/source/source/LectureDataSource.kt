package `in`.hangang.hangang.data.source.source

interface LectureDataSource {
    fun getLectureRanking(major: String)
    fun getRecommendedLectures()
    fun getRecentLectures()
}