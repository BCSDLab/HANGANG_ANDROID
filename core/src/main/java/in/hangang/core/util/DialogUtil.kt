package `in`.hangang.core.util

import `in`.hangang.core.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialog

object DialogUtil {
    fun makeSimpleDialog(
        context: Context,
        title: String? = null,
        message: String,
        positiveButtonText: String = "OK",
        negativeButtonText: String? = null,
        positiveButtonOnClickListener: DialogInterface.OnClickListener,
        negativeButtonOnClickListener: DialogInterface.OnClickListener? = null,
        cancelable: Boolean = true
    ): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_simple, null)
        val textViewTitle = view.findViewById<TextView>(R.id.text_view_title)
        val textViewMessage = view.findViewById<TextView>(R.id.text_view_message)
        val buttonPositive = view.findViewById<Button>(R.id.button_positive)
        val buttonNegative = view.findViewById<Button>(R.id.button_negative)

        val builder = AlertDialog.Builder(context)
            .setView(view)

        val dialog = builder.create()
        dialog.setCancelable(cancelable)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        textViewMessage.text = message

        buttonPositive.text = positiveButtonText
        buttonPositive.setOnClickListener {
            positiveButtonOnClickListener.onClick(dialog, BUTTON_POSITIVE)
        }

        if (negativeButtonText == null)
            buttonNegative.visibility = View.GONE
        else {
            buttonNegative.text = negativeButtonText
            buttonNegative.setOnClickListener {
                negativeButtonOnClickListener?.onClick(dialog, BUTTON_NEGATIVE)
            }
        }

        if (title == null)
            textViewTitle.visibility = View.GONE
        else
            textViewTitle.text = title

        return dialog
    }

    fun makeViewDialog(
        context: Context,
        title: String? = null,
        view: View,
        positiveButtonText: String = "OK",
        negativeButtonText: String? = null,
        positiveButtonOnClickListener: DialogInterface.OnClickListener,
        negativeButtonOnClickListener: DialogInterface.OnClickListener? = null,
        cancelable: Boolean = true
    ): Dialog {
        val mainView = LayoutInflater.from(context).inflate(R.layout.dialog_view, null)
        val textViewTitle = mainView.findViewById<TextView>(R.id.text_view_title)
        val buttonPositive = mainView.findViewById<Button>(R.id.button_positive)
        val buttonNegative = mainView.findViewById<Button>(R.id.button_negative)
        val container = mainView.findViewById<FrameLayout>(R.id.view_container)

        val builder = AlertDialog.Builder(context)
            .setView(mainView)

        val dialog = builder.create()
        dialog.setCancelable(cancelable)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(view)

        buttonPositive.text = positiveButtonText
        buttonPositive.setOnClickListener {
            positiveButtonOnClickListener.onClick(dialog, BUTTON_POSITIVE)
        }

        if (negativeButtonText == null)
            buttonNegative.visibility = View.GONE
        else {
            buttonNegative.text = negativeButtonText
            buttonNegative.setOnClickListener {
                negativeButtonOnClickListener?.onClick(dialog, BUTTON_NEGATIVE)
            }
        }

        if (title == null)
            textViewTitle.visibility = View.GONE
        else
            textViewTitle.text = title

        return dialog
    }

    fun makeListBottomSheet(
        context: Context,
        items: List<CharSequence>,
        footerView: View? = null,
        onItemClickListener: AdapterView.OnItemClickListener? = null
    ): BottomSheetDialog {

        val bottomSheet = BottomSheetDialog(context, R.style.HangangBottomSheetDialogTheme)
        val bottomSheetView =
            LayoutInflater.from(context).inflate(R.layout.layout_simple_list_bottom_sheet, null)

        if (footerView != null)
            bottomSheetView.findViewById<LinearLayout>(R.id.container).addView(footerView)

        bottomSheetView.findViewById<ListView>(R.id.simple_list_bottom_sheet_list_view).apply {
            adapter =
                ArrayAdapter(context, R.layout.simple_text_item_1, android.R.id.text1, items)
            divider = null

            setOnItemClickListener { parent, view, position, id ->
                onItemClickListener?.onItemClick(parent, view, position, id)
                bottomSheet.dismiss()
            }
        }

        bottomSheet.setContentView(bottomSheetView)
        return bottomSheet
    }
}