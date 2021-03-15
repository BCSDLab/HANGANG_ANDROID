package `in`.hangang.hangang.ui.timetable.contract

import `in`.hangang.hangang.ui.timetable.activity.TimetableAddActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class TimeTableAddActivityContract : ActivityResultContract<Void?, Boolean>() {
    companion object {
        const val TIMETABLE_ADDED = "TIMETABLE_ADDED"
    }

    override fun createIntent(context: Context, input: Void?): Intent {
        return Intent(context, TimetableAddActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return intent?.extras?.getBoolean(TIMETABLE_ADDED) ?: false
    }
}