package `in`.hangang.core

import `in`.hangang.core.dialog.SimpleDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

open class ActivityBase : AppCompatActivity() {
    var simpleDialog: SimpleDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showSimpleDialog(
        message: String,
        positiveButtonText: String? = null,
        negativeButtonString: String? = null,
        positiveButtonOnClickListener: View.OnClickListener? = null,
        negativeButtonOnClickListener: View.OnClickListener? = null
    ) {
        if (simpleDialog == null) {
            simpleDialog = SimpleDialog(
                this,
                message,
                positiveButtonText,
                negativeButtonString,
                positiveButtonOnClickListener,
                negativeButtonOnClickListener
            )

            simpleDialog!!.show()
        }
    }
}