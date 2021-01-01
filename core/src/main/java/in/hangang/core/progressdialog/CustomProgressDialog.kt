package `in`.hangang.core.progressdialog

import `in`.hangang.core.R
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask

class CustomProgressDialog(context: Context?, private val message: String) :
    AsyncTask<Void?, Void?, Void?>() {
    private val TAG = "CustomProgressDialog"
    private var progressDialog: ProgressDialog?
    override fun onPreExecute() {
        progressDialog!!.setMessage(message)
        progressDialog!!.show()
    }

    override fun onPostExecute(aVoid: Void?) {
        if (progressDialog != null) progressDialog!!.dismiss()
    }

    override fun onCancelled(aVoid: Void?) {
        if (progressDialog != null) progressDialog!!.dismiss()
        super.onCancelled(aVoid)
        progressDialog = null
    }

    init {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.isIndeterminate = true
    }

    override fun doInBackground(vararg params: Void?): Void? {
        TODO("Not yet implemented")
        while (true) {
            if (isCancelled) {
                break
            }
        }
        return null
    }
}
