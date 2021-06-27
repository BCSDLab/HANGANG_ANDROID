package `in`.hangang.hangang.ui.mypage.contract

import `in`.hangang.hangang.ui.mypage.activity.MyScrapLectureReviewActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class MyScrapLectureReviewActivityContract : ActivityResultContract<Void, Int>() {
    override fun createIntent(context: Context, input: Void?): Intent {
        return Intent(context, MyScrapLectureReviewActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }
}