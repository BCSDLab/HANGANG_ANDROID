package `in`.hangang.hangang.ui.lecturebank.contract

import `in`.hangang.hangang.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract


class LectureBankImagePickerContract : LectureBankFilePickerContract() {
    override fun createIntent(context: Context, input: Void?): Intent {
        return Intent.createChooser(
            Intent(Intent.ACTION_PICK).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                type = "image/*"
            },
            context.getString(R.string.description_image_chooser)
        )
    }
}