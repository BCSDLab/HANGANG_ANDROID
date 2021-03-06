package `in`.hangang.hangang.ui.lecturebank.contract

import `in`.hangang.core.util.requireAndroidVersion
import `in`.hangang.hangang.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract


open class LectureBankFilePickerContract : ActivityResultContract<Void?, List<Uri>>() {
    override fun createIntent(context: Context, input: Void?): Intent {
            return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        return intent?.clipData?.let {
            val list = mutableListOf<Uri>()
            for (i in 0 until it.itemCount) {
                list.add(
                    it.getItemAt(i).uri
                )
            }
            list.toList()
        } ?: intent?.data?.let {
            listOf(it)
        } ?: emptyList()
    }
}