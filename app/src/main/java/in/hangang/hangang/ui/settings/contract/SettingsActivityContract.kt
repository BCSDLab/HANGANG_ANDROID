package `in`.hangang.hangang.ui.settings.contract

import `in`.hangang.hangang.ui.settings.activity.SettingsActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class SettingsActivityContract : ActivityResultContract<Void?, Int>() {
    override fun createIntent(context: Context, input: Void?): Intent {
        return Intent(context, SettingsActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return resultCode
    }

}