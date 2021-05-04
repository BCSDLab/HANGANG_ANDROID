package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.data.source.LectureReviewPagingSource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.*

class LectureReviewListViewModel(private val lectureRepository: LectureRepository) :
    ViewModelBase() {
    private val _rankingLectureList = MutableLiveData<PagingData<RankingLectureItem>>()
    val rankingLectureList: LiveData<PagingData<RankingLectureItem>> get() = _rankingLectureList
    var currentResult: Flowable<PagingData<RankingLectureItem>>? = null
    var selectedMajorList = ArrayList<String>()
    var selectedMajorListDefault = ArrayList<String>()
    val isLoadEnd = MutableLiveData<Boolean>()

    init {
        selectedMajorListDefault.add("")
    }

    fun isAddMajorPossible(): Boolean {
        return selectedMajorList.size < 2
    }

    fun getLectureReviewList(majors: ArrayList<String>): Flowable<PagingData<RankingLectureItem>> {

        val newResult = lectureRepository
            .getLectureReviewList(majors)
            .cachedIn(viewModelScope)
        currentResult = newResult
        newResult.subscribe {
            _rankingLectureList.value = it
        }
            .addTo(compositeDisposable)
        return newResult
    }
}