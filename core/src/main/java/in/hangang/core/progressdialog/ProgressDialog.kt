package `in`.hangang.core.progressdialog

import `in`.hangang.core.R
import android.app.Activity
import android.app.AlertDialog
import android.os.AsyncTask
import android.widget.TextView

class ProgressDialog(val activity: Activity, val message: String) :
    AsyncTask<Void?, Void?, Void?>() {
    private var progressDialog: AlertDialog? = null
    lateinit private var progressMessage: TextView
    private val TAG = "CustomProgressDialog"

    init {
        val view = activity.layoutInflater.inflate(R.layout.layout_progressbar, null)

        var builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setView(view)

        progressMessage = view.findViewById(R.id.progress_message)
        progressMessage.text = message

        progressDialog = builder.create()
    }

    fun show() {
        progressDialog?.show()
    }

    fun dismiss() {
        progressDialog?.dismiss()
    }

    override fun onPreExecute() {

        progressDialog?.show()
    }

    override fun onPostExecute(aVoid: Void?) {
        if (progressDialog != null) progressDialog?.dismiss()
    }

    override fun onCancelled(aVoid: Void?) {
        if (progressDialog != null) progressDialog!!.dismiss()
        super.onCancelled(aVoid)
        progressDialog = null
    }

    override fun doInBackground(vararg params: Void?): Void? {
        while (true) {
            if (isCancelled) {
                break
            }
        }
        return null
    }
}