package `in`.hangang.hangang.data.entity

import java.util.*

data class TimeTableWithLecture(
    val id: Int,
    val tableName: String?,
    val tableSemesterDate: String,
    val lectureList: List<LectureTimeTable>
){
    companion object {
        val EMPTY = TimeTableWithLecture(
            -1, "", "", emptyList()
        )
    }
}

fun TimeTableWithLecture.toTimeTable() = TimeTable(
        id = this.id,
        name = this.tableName,
        semesterDateId = this.tableSemesterDate.toInt()
)