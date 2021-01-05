package `in`.hangang.core.progressdialog

import `in`.hangang.core.R
import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView

class ProgressDialog(val activity: Activity, val message: String){
    private val TAG = "CustomProgressDialog"
    private var progressDialog: AlertDialog? = null
    lateinit private var progressMessage: TextView


    init {
        val view = activity.layoutInflater.inflate(R.layout.layout_progressbar, null)  //findviewbyId를 하기위해 view를 가져온다.

        var builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setView(view)       //AlertDialog의 화면을 설정

        progressMessage = view.findViewById(R.id.progress_message)
        progressMessage.text = message      //progressMessage 설정

        progressDialog = builder.create()
    }

    fun show() {
        progressDialog?.show()
    }

    fun dismiss() {
        progressDialog?.dismiss()
    }

}