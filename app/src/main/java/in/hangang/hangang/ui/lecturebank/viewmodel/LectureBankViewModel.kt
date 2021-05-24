package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.constant.LECTURE_BANKS_ORDER_BY_ID
import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.data.lecturebank.LectureBankFilter
import `in`.hangang.hangang.data.source.LectureBankRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.kotlin.addTo

class LectureBankViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {
    private val _keyword = MutableLiveData<String?>()
    private val _department = MutableLiveData<String?>()
    private val _lectureBankFilter = MutableLiveData<LectureBankFilter>()

    private val _lectureBankPagingData = MutableLiveData<PagingData<LectureBank>>()

    val lectureBankPagingData : LiveData<PagingData<LectureBank>> get() = _lectureBankPagingData
    val lectureBankFilter : LiveData<LectureBankFilter> get() = _lectureBankFilter

    fun getLectureBanks() {
        lectureBankRepository.getLectureBanks(
            _lectureBankFilter.value?.categories,
            _department.value,
            _keyword.value,
            _lectureBankFilter.value?.order ?: LECTURE_BANKS_ORDER_BY_ID
        )
            .cachedIn(viewModelScope)
            .subscribe {
                _lectureBankPagingData.value = it
            }
            .addTo(compositeDisposable)
    }

    fun setFilter(lectureBankFilter: LectureBankFilter) {
        _lectureBankFilter.value = lectureBankFilter
        getLectureBanks()
    }

    fun setDepartment(department: String?) {
        _department.value = department
        getLectureBanks()
    }
}