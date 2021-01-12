package `in`.hangang.core.util

import `in`.hangang.core.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialog

object DialogUtil {
    fun makeSimpleDialog(
            context: Context,
            title: String? = null,
            message: String,
            positiveButtonText: String = "OK",
            negativeButtonText: String? = null,
            positiveButtonOnClickListener: View.OnClickListener,
            negativeButtonOnClickListener: View.OnClickListener? = null,
            cancelable: Boolean = true
    ): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_simple, null)
        val textViewTitle = view.findViewById<TextView>(R.id.text_view_title)
        val textViewMessage = view.findViewById<TextView>(R.id.text_view_message)
        val buttonPositive = view.findViewById<Button>(R.id.button_positive)
        val buttonNegative = view.findViewById<Button>(R.id.button_negative)

        textViewMessage.text = message

        buttonPositive.text = positiveButtonText
        buttonPositive.setOnClickListener(positiveButtonOnClickListener)

        if (negativeButtonText == null)
            buttonNegative.visibility = View.GONE
        else {
            buttonNegative.text = negativeButtonText
            buttonNegative.setOnClickListener(negativeButtonOnClickListener)
        }

        val builder = AlertDialog.Builder(context)
                .setView(view)

        if (title == null)
            textViewTitle.visibility = View.GONE
        else
            textViewTitle.text = title

        val dialog = builder.create()
        dialog.setCancelable(cancelable)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun makeListBottomSheet(
            context: Context,
            items: List<CharSequence>,
            footerView: View? = null,
            onItemClickListener: AdapterView.OnItemClickListener? = null): BottomSheetDialog {

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