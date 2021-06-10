package `in`.hangang.hangang.data.evaluation

data class TimeTableWithLecture(
    val id: Int,
    val tableName: String?,
    val tableSemesterDate: String,
    val lectureList: List<LectureTimeTable>
)

fun TimeTableWithLecture.toTimeTable() = TimeTable(
    id = this.id,
    name = this.tableName,
    semesterDateId = this.tableSemesterDate.toInt()
)