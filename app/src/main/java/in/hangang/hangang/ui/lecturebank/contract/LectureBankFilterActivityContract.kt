package `in`.hangang.hangang.ui.lecturebank.contract

import `in`.hangang.hangang.data.entity.lecturebank.LectureBankFilter
import `in`.hangang.hangang.ui.lecturebank.activity.LectureBankFilterActivity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class LectureBankFilterActivityContract : ActivityResultContract<LectureBankFilter, LectureBankFilter?>() {
    override fun createIntent(context: Context, input: LectureBankFilter?): Intent {
        return Intent(context, LectureBankFilterActivity::class.java).apply {
            putExtra(INTENT_EXTRA_FILTER, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): LectureBankFilter? {
        if(resultCode == RESULT_OK) return intent?.getParcelableExtra(INTENT_EXTRA_FILTER) as? LectureBankFilter
        return null
    }

    companion object {
        const val INTENT_EXTRA_FILTER = "INTENT_EXTRA_FILTER"
    }
}
