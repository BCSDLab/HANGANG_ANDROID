package `in`.hangang.core.progressdialog

import `in`.hangang.core.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView


class ProgressDialog(context: Context, message: String) {
    companion object {
        private const val TAG = "CustomProgressDialog"
    }

    private val progressDialog: Dialog = AlertDialog.Builder(context, R.style.Theme_Hangang_AlertDialog).apply {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_progressbar, null)
        setCancelable(false)
        setView(view)       //AlertDialog의 화면을 설정

        val progressMessage = view.findViewById<TextView>(R.id.progress_message)
        progressMessage.text = message      //progressMessage 설정
    }.create()

    fun show() {
        progressDialog.show()
    }

    fun hide() {
        progressDialog.hide()
    }

    fun dismiss() {
        progressDialog.dismiss()
    }
}