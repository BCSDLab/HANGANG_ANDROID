package `in`.hangang.hangang.data.source

class TimetableRepository(
        private val timetableLocalDataSource: TimetableDataSource,
        private val timetableRemoteDataSource: TimetableDataSource
) : TimetableDataSource {
    override fun getTimetable() {
        TODO("Not yet implemented")
    }
}