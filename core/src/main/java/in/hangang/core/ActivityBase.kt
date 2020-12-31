package `in`.hangang.core

import `in`.hangang.core.util.DialogUtil
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

open class ActivityBase : AppCompatActivity() {
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showSimpleDialog(
            title: String? = null,
            message: String = "",
            positiveButtonText: String = "OK",
            negativeButtonText: String? = null,
            positiveButtonOnClickListener: View.OnClickListener? = null,
            negativeButtonOnClickListener: View.OnClickListener? = null,
            cancelable: Boolean = true
    ) {
        if (dialog == null) {
            dialog = DialogUtil.makeSimpleDialog(this,
                    title,
                    message,
                    positiveButtonText,
                    negativeButtonText,
                    positiveButtonOnClickListener,
                    negativeButtonOnClickListener,
                    cancelable)
            dialog!!.show()
        }
    }

    fun dismissSimpleDialog() {
        dialog?.let {
            it.dismiss()
            dialog = null
        }
    }

    fun startActivity(javaClass: Class<*>) {
        val intent = Intent(this, javaClass)
        startActivity(intent)
    }

    fun startActivity(javaClass: Class<*>, extras: (Bundle) -> Unit) {
        val intent = Intent(this, javaClass)
        val bundle = Bundle()
        extras(bundle)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}