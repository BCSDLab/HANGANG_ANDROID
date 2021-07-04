package `in`.hangang.hangang.ui.lecturebank.contract

import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.ui.lecturebank.activity.LectureBankSelectLectureActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class LectureBankEditorSelectLectureActivityContract : ActivityResultContract<Lecture?, LectureBankEditorSelectLectureActivityContract.Result>() {

    override fun createIntent(context: Context, input: Lecture?): Intent {
        return Intent(context, LectureBankSelectLectureActivity::class.java).apply {
            putExtra(INPUT_LECTURE, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        return Result(resultCode, intent?.extras?.getParcelable(OUTPUT_LECTURE))
    }

    data class Result(
        val resultCode: Int,
        val lecture: Lecture?
    )

    companion object {
        const val INPUT_LECTURE = "INPUT_LECTURE"
        const val OUTPUT_LECTURE = "OUTPUT_LECTURE"
    }
}