package `in`.hangang.core.util

import `in`.hangang.core.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView

object DialogUtil {
    fun makeSimpleDialog(
            context: Context,
            title: String? = null,
            message: String = "",
            positiveButtonText: String = "OK",
            negativeButtonText: String? = null,
            positiveButtonOnClickListener: View.OnClickListener? = null,
            negativeButtonOnClickListener: View.OnClickListener? = null,
            cancelable: Boolean = true
    ) : Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_simple, null)
        val textViewMessage = view.findViewById<TextView>(R.id.text_view_message)
        val buttonPositive = view.findViewById<Button>(R.id.button_positive)
        val buttonNegative = view.findViewById<Button>(R.id.button_negative)

        textViewMessage.text = message

        buttonPositive.text = positiveButtonText
        buttonPositive.setOnClickListener(positiveButtonOnClickListener)

        if(negativeButtonText == null)
            buttonNegative.visibility = View.GONE
        else {
            buttonNegative.text = negativeButtonText
            buttonNegative.setOnClickListener(negativeButtonOnClickListener)
        }

        val builder = AlertDialog.Builder(context)
                .setView(view)

        if(title != null)
            builder.setTitle(title)

        val dialog = builder.create()
        dialog.setCancelable(cancelable)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}