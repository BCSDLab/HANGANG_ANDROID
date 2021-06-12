package `in`.hangang.hangang.data.entity

data class MainTimeTable(
        val id: Int,
        val tableName: String?,
        val tableSemesterDate: String,
        val lectureList: List<LectureTimeTable>
)