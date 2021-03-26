package `in`.hangang.hangang.ui.timetable.contract

import `in`.hangang.hangang.data.entity.LectureFilter
import `in`.hangang.hangang.ui.timetable.activity.TimetableLectureFilterActivity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class TimeTableLectureFilterActivityContract : ActivityResultContract<LectureFilter?, TimeTableLectureFilterActivityContract.Result>() {

    data class Result(
            val apply: Boolean,
            val lectureFilter: LectureFilter?
    )

    companion object {
        const val TIMETABLE_LECTURE_FILTER = "TIMETABLE_LECTURE_FILTER"
    }

    override fun createIntent(context: Context, input: LectureFilter?): Intent {
        return Intent(context, TimetableLectureFilterActivity::class.java).apply {
            putExtra(TIMETABLE_LECTURE_FILTER, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        return Result(
                resultCode == RESULT_OK,
                intent?.extras?.getParcelable(TIMETABLE_LECTURE_FILTER)
        )
    }
}