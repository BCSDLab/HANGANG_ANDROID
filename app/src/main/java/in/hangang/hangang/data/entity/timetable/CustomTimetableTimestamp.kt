package `in`.hangang.hangang.data.entity.timetable

data class CustomTimetableTimestamp(
        val week: Int = 0, //0 1 2 3 4 5 6 월 ~ 일
        val startTime: Pair<Int, Int>, //<(Hour), (Minute)
        val endTime: Pair<Int, Int>
)