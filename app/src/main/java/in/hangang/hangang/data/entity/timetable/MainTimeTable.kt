package `in`.hangang.hangang.data.entity.timetable

data class MainTimeTable(
        val id: Int,
        val tableName: String?,
        val tableSemesterDate: String,
        val lectureList: List<LectureTimeTable>
)