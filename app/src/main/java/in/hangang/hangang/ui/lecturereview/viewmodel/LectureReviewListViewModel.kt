package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.data.source.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.kotlin.addTo

class LectureReviewListViewModel(private val lectureRepository: LectureRepository) : ViewModelBase() {
    private val _rankingLectureList = MutableLiveData<PagingData<RankingLectureItem>>()
    val rankingLectureList: LiveData<PagingData<RankingLectureItem>> get() = _rankingLectureList
    var currentResult: Flowable<PagingData<RankingLectureItem>>? = null
    fun getLectureReviewList(major: String): Flowable<PagingData<RankingLectureItem>> {
        val newResult = lectureRepository
            .getLectureReviewList(major)
            .cachedIn(viewModelScope)
        currentResult = newResult
        newResult.subscribe { _rankingLectureList.value = it }
            .addTo(compositeDisposable)
        return newResult
    }
}