package `in`.hangang.core.base.fragment

import `in`.hangang.core.base.activity.ActivityBase
import `in`.hangang.core.base.activity.getColorFromAttr
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.Exception

open class FragmentBase : Fragment() {

    protected val compositeDisposable = CompositeDisposable()

    fun addDisposable(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    inline fun requireWriteStorage(crossinline result: () -> Unit) = try {
        (requireActivity() as ActivityBase).requireWriteStorage { result() }
    } catch (e: Exception) {
        e.printStackTrace()
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