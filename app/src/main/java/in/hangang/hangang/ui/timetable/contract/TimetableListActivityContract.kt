package `in`.hangang.hangang.ui.timetable.contract

import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.ui.timetable.activity.TimetableListActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class TimetableListActivityContract : ActivityResultContract<TimeTable, TimetableListActivityContract.Result>() {

    companion object {
        const val SELECTED_TIMETABLE = "SELECTED_TIMETABLE"
        const val TIMETABLE_LIST_CHANGED = "TIMETABLE_LIST_CHANGED"
    }

    class Result(
        val resultCode: Int,
        val selectedTimetable: TimeTable?,
        val timetableListChanged: Boolean = false
    )

    override fun createIntent(context: Context, input: TimeTable): Intent {
        return Intent(context, TimetableListActivity::class.java).apply {
            putExtra(SELECTED_TIMETABLE, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        return Result(
            resultCode = resultCode,
            selectedTimetable = intent?.extras?.getParcelable(SELECTED_TIMETABLE),
            timetableListChanged = intent?.extras?.getBoolean(TIMETABLE_LIST_CHANGED) ?: false
        )
    }
}