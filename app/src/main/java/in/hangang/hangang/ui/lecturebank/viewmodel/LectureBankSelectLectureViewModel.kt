package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.source.repository.LectureRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.kotlin.addTo

class LectureBankSelectLectureViewModel(
    private val lectureRepository: LectureRepository
) : ViewModelBase() {

    private var department : String? = null
    private var keyword : String? = null

    private val _lectureBankLectureList = MutableLiveData<PagingData<Lecture>>()

    val lectureBankLectureList : LiveData<PagingData<Lecture>> get() = _lectureBankLectureList

    fun getLectureBankLectureList() {
        lectureRepository.getLectureList(
            department = department,
            keyword = keyword
        )
            .cachedIn(viewModelScope)
            .subscribe { _lectureBankLectureList.value = it }
            .addTo(compositeDisposable)
    }

    fun setDepartment(department: String?) {
        this.department = department
        getLectureBankLectureList()
    }

    fun setKeyword(keyword: String?) {
        this.keyword = keyword
        getLectureBankLectureList()
    }
}