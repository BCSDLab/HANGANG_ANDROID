package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.constant.SORT_BY_TOTAL_RATING
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.LectureRepository
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

class LectureReviewListViewModel(private val lectureRepository: LectureRepository) :
    ViewModelBase() {
    private val _rankingLectureList = MutableLiveData<PagingData<RankingLectureItem>>()
    val rankingLectureList: LiveData<PagingData<RankingLectureItem>> get() = _rankingLectureList
    var currentResult: Flowable<PagingData<RankingLectureItem>>? = null
    var selectedMajorList = ArrayList<String>()
    var selectedMajorListDefault = ArrayList<String>()
    private val _isGetScrapList = MutableLiveData<Boolean>()
    val isGetScrapList: LiveData<Boolean> get() = _isGetScrapList
    var scrapLectureList = ArrayList<RankingLectureItem>()
    var filterSort = SORT_BY_TOTAL_RATING
    var filterType = ArrayList<String>()
    var filterHashTag = ArrayList<Int>()
    var keyword: String? = null
    var filterTypeIsChecked = booleanArrayOf(false, false, false, false, false, false, false, false)
    var filterHashTagIsChecked =
        booleanArrayOf(false, false, false, false, false, false, false, false, false)

    fun getTempFilterType(): ArrayList<String>{
        var tempFilterType = ArrayList<String>()
        filterType.addAll(filterType)
        return tempFilterType
    }
    fun getTempFilterSort(): String{
        var tempSort = filterSort
        return tempSort
    }
    fun getTempHashTag(): ArrayList<Int>{
        var tempHashTag = ArrayList<Int>()
        tempHashTag.addAll(filterHashTag)
        return tempHashTag
    }
    init {
        selectedMajorListDefault.add("")
    }

    fun isAddMajorPossible(): Boolean {
        return selectedMajorList.size < 2
    }

    fun getLectureReviewList(majors: ArrayList<String>) {
        for (i in majors) {
            LogUtil.e(i.toString())
        }
        for (i in filterType) {
            LogUtil.e(i.toString())
        }
        lectureRepository
            .getFilteredLectureReviewList(majors, filterType, filterHashTag, filterSort, keyword)
            .cachedIn(viewModelScope)
            .subscribe {
                _rankingLectureList.value = it
            }
            .addTo(compositeDisposable)
    }

    fun getScrapList() {
        lectureRepository.getScrapedLecture()
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({ scrapList ->
                scrapLectureList = scrapList
                _isGetScrapList.value = true
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
            .addTo(compositeDisposable)
    }



}