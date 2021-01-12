package `in`.hangang.hangang.ui.dashboard

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo

class DashBoardViewModel : ViewModelBase() {
    private val users: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also {
            getData()
        }
    }

    fun getUsers(forceFetch: Boolean = false): LiveData<Boolean> {
        if (forceFetch) getData()
        return users
    }


    private fun getData() {
        Single.just(" ")
            .map { Thread.sleep(5000L) }
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe()
            .addTo(compositeDisposable)
    }
}