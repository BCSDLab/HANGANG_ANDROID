package `in`.hangang.core.dialog

import `in`.hangang.core.R
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

class SimpleDialog(context: Context,
                   val message: String,
                   val positiveButtonText: String? = null,
                   val negativeButtonString: String? = null,
                   val cancelable: Boolean = true,
                   val positiveButtonOnClickListener: View.OnClickListener? = null,
                   val negativeButtonOnClickListener: View.OnClickListener? = null) : Dialog(context, R.style.Theme_Hangang_Dialog) {
    private lateinit var textViewMessage: TextView
    private lateinit var buttonPositive: Button
    private lateinit var buttonNegative: Button

    private val onClickDismiss = View.OnClickListener {
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_simple)

        setCancelable(cancelable)

        initView()
    }

    private fun initView() {
        textViewMessage = findViewById(R.id.text_view_message)
        buttonPositive = findViewById(R.id.button_positive)
        buttonNegative = findViewById(R.id.button_negative)

        setValue(message, positiveButtonText, negativeButtonString, positiveButtonOnClickListener, negativeButtonOnClickListener)
    }

    private fun setValue(
            message: String,
            positiveButtonText: String? = null,
            negativeButtonString: String? = null,
            positiveButtonOnClickListener: View.OnClickListener? = null,
            negativeButtonOnClickListener: View.OnClickListener? = null
    ) {
        textViewMessage.text = message

        if (positiveButtonText == null)
            buttonPositive.visibility = View.GONE
        else
            buttonPositive.text = positiveButtonText
        if (negativeButtonString == null)
            buttonNegative.visibility = View.GONE
        else
            buttonNegative.text = negativeButtonString

        buttonPositive.setOnClickListener(positiveButtonOnClickListener ?: onClickDismiss)
        buttonNegative.setOnClickListener(negativeButtonOnClickListener ?: onClickDismiss)
    }

    override fun show() {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT

        super.show()
        window?.attributes = layoutParams
    }
}