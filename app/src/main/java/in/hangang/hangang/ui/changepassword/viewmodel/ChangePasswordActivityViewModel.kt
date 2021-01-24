package `in`.hangang.hangang.ui.changepassword.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ChangePasswordActivityViewModel : ViewModelBase() {
    private val _currentPage = MutableLiveData(0)

    val currentPage : LiveData<Int> get() = _currentPage

    fun nextPage() {
        _currentPage.value = (_currentPage.value ?: 0) + 1
    }
}