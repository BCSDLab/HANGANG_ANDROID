package `in`.hangang.hangang.ui.mypage.contract

import `in`.hangang.hangang.ui.mypage.activity.MyPagePointRecordActivity
import `in`.hangang.hangang.ui.mypage.activity.MyPagePurchasedBankActivity
import `in`.hangang.hangang.ui.mypage.activity.MyScrapActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class MyScrapActivityContract : ActivityResultContract<Void, Int>() {
    override fun createIntent(context: Context, input: Void?): Intent {
        return Intent(context, MyScrapActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }
}