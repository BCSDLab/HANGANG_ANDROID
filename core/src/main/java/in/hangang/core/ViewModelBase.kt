package `in`.hangang.core

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class ViewModelBase : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}