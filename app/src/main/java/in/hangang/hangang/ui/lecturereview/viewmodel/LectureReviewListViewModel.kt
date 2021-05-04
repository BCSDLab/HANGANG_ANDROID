package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.data.source.LectureReviewPagingSource
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
import java.util.*
import kotlin.collections.ArrayList

class LectureReviewListViewModel(private val lectureRepository: LectureRepository) :
    ViewModelBase() {
    private val _rankingLectureList = MutableLiveData<PagingData<RankingLectureItem>>()
    val rankingLectureList: LiveData<PagingData<RankingLectureItem>> get() = _rankingLectureList
    var currentResult: Flowable<PagingData<RankingLectureItem>>? = null
    var selectedMajorList = ArrayList<String>()
    var selectedMajorListDefault = ArrayList<String>()
    val isLoadEnd = MutableLiveData<Boolean>()
    var scrapLectureList = ArrayList<RankingLectureItem>()

    init {
        selectedMajorListDefault.add("")
    }

    fun isAddMajorPossible(): Boolean {
        return selectedMajorList.size < 2
    }

    fun getLectureReviewList(majors: ArrayList<String>) {
        lectureRepository.getScrapedLecture()
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({ scrapList ->
                scrapLectureList = scrapList
                getLectureList(majors)
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
            .addTo(compositeDisposable)
    }

    fun getLectureList(majors: ArrayList<String>) {
        lectureRepository
            .getLectureReviewList(majors)
            .cachedIn(viewModelScope)
            .subscribe {
                _rankingLectureList.value = it
            }
            .addTo(compositeDisposable)
    }
}