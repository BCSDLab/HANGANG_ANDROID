package `in`.hangang.core.base.fragment

import `in`.hangang.core.R
import `in`.hangang.core.base.activity.ActivityBase
import `in`.hangang.core.base.activity.getColorFromAttr
import `in`.hangang.core.progressdialog.IProgressDialog
import `in`.hangang.core.progressdialog.ProgressDialog
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class FragmentBase : Fragment(), IProgressDialog {

    protected val compositeDisposable = CompositeDisposable()
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(requireContext(), getString(R.string.loading)) }

    fun addDisposable(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun onDestroy() {
        progressDialog.dismiss()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        super.onDestroy()
    }

    inline fun requireWriteStorage(crossinline result: () -> Unit) = try {
        (requireActivity() as ActivityBase).requireWriteStorage { result() }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun hideProgressDialog() {
        progressDialog.hide()
    }
}

@ColorInt
fun Fragment.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
): Int {
    return activity?.getColorFromAttr(attrColor, typedValue, resolveRefs) ?: Color.BLACK
}