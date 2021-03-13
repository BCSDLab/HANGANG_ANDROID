package `in`.hangang.core.base.activity

import `in`.hangang.core.R
import `in`.hangang.core.progressdialog.IProgressDialog
import `in`.hangang.core.progressdialog.ProgressDialog
import `in`.hangang.core.util.DialogUtil
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class ActivityBase : AppCompatActivity(), IProgressDialog {
    private val compositeDisposable = CompositeDisposable()
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun addDisposable(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
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

    fun startActivityForResult(
            javaClass: Class<*>,
            requestCode: Int,
            extras: ((Bundle) -> Unit)? = null
    ) {
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
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

    override fun showProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = ProgressDialog(this, getString(R.string.loading))
        progressDialog?.show()
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}

@ColorInt
fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Context.showSimpleDialog(
        title: String? = null,
        message: String,
        positiveButtonText: String = "OK",
        negativeButtonText: String? = null,
        positiveButtonOnClickListener: DialogInterface.OnClickListener,
        negativeButtonOnClickListener: DialogInterface.OnClickListener? = null,
        cancelable: Boolean = true
) = DialogUtil.makeSimpleDialog(
        this,
        title,
        message,
        positiveButtonText,
        negativeButtonText,
        positiveButtonOnClickListener,
        negativeButtonOnClickListener,
        cancelable
).show()