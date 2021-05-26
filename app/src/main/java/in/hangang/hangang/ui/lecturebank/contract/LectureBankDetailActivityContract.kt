package `in`.hangang.hangang.ui.lecturebank.contract

import `in`.hangang.hangang.ui.lecturebank.activity.LectureBankDetailActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class LectureBankDetailActivityContract : ActivityResultContract<Int, Int>() {
    override fun createIntent(context: Context, input: Int?): Intent {
        return Intent(context, LectureBankDetailActivity::class.java).apply {
            putExtra(LECTURE_BANK_ID, input ?: -1)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return 1
    }

    companion object {
        const val LECTURE_BANK_ID = "LECTURE_BANK_ID"
    }
}