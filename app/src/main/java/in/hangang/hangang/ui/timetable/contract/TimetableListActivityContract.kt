package `in`.hangang.hangang.ui.timetable.contract

import `in`.hangang.hangang.ui.timetable.activity.TimetableListActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class TimetableListActivityContract : ActivityResultContract<Int, TimetableListActivityContract.Result>() {

    inner class Result(
            val selectedTimetableId : Int?,
            val timetableListChanged : Boolean = false
    )

    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(context, TimetableListActivity::class.java).apply {
            putExtra("selectedTimeTableId", input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        return Result(
                selectedTimetableId = intent?.extras?.getInt("selectedTimeTableId"),
                timetableListChanged = intent?.extras?.getBoolean("timetableListChanged") ?: false
        )
    }
}