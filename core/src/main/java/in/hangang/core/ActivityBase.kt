package `in`.hangang.core

import `in`.hangang.core.util.DialogUtil
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable

open class ActivityBase<T : ViewDataBinding>(@LayoutRes private val layoutId: Int) : AppCompatActivity() {
    protected var dialog: Dialog? = null
    protected lateinit var binding : T
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    fun showSimpleDialog(
            title: String? = null,
            message: String,
            positiveButtonText: String = "OK",
            negativeButtonText: String? = null,
            positiveButtonOnClickListener: View.OnClickListener,
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

    fun startActivity(javaClass: Class<*>, extras: ((Bundle) -> Unit)? = null) {
        val intent = Intent(this, javaClass)
        if (extras != null) {
            val bundle = Bundle()
            extras(bundle)
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun startActivityForResult(javaClass: Class<*>, requestCode: Int, extras: ((Bundle) -> Unit)? = null) {
        val intent = Intent(this, javaClass)
        if (extras != null) {
            val bundle = Bundle()
            extras(bundle)
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
        binding.unbind()
    }
}