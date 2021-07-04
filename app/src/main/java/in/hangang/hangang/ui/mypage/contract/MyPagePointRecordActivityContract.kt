package `in`.hangang.hangang.ui.mypage.contract

import `in`.hangang.hangang.ui.mypage.activity.MyPagePointRecordActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class MyPagePointRecordActivityContract : ActivityResultContract<Int, Int>() {
    override fun createIntent(context: Context, input: Int?): Intent {
        return Intent(context, MyPagePointRecordActivity::class.java).apply {
            putExtra("point", input ?: 0)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }
}