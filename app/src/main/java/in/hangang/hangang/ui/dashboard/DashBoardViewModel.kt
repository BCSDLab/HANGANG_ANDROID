package `in`.hangang.hangang.ui.dashboard

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class DashBoardViewModel(private val userRepository: UserRepository) : ViewModelBase() {
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
        userRepository.emailPasswordCheck("jason")
            .toSingleConvert()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({ data -> LogUtil.d(data.toString()) },
                { error -> LogUtil.e(error.message) })
            .addTo(compositeDisposable)
    }
}