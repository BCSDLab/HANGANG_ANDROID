package `in`.hangang.hangang.ui.lecturebank.contract

import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.ui.lecturebank.activity.LectureBankEditorActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class LectureBankEditorActivityContract : ActivityResultContract<LectureBank?, Int>() {
    override fun createIntent(context: Context, input: LectureBank?): Intent {
        return Intent(context, LectureBankEditorActivity::class.java).apply {
            putExtra(LECTURE_BANK, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }

    companion object {
        const val LECTURE_BANK = "LECTURE_BANK"

        const val RESULT_UPLOADED = 101
    }
}