package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.constant.SORT_BY_TOTAL_RATING
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
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
import java.util.*
import kotlin.collections.ArrayList

class LectureReviewListViewModel(private val lectureRepository: LectureRepository) :
    ViewModelBase() {

    private val _rankingLectureList = MutableLiveData<PagingData<RankingLectureItem>>()
    val rankingLectureList: LiveData<PagingData<RankingLectureItem>> get() = _rankingLectureList

    private val _lectureListItemCount = MutableLiveData<Int>()
    val lectureLsitItemCount: LiveData<Int> get() = _lectureListItemCount


    var selectedMajorList = ArrayList<String>()             //선택된 학부 리스트
    var selectedMajorListDefault = ArrayList<String>()      //디폴드 학부 리스트 - 선택한 학부가 없을 경우 빈 리스트를 보내야 하기 때문에
    var filterSort = SORT_BY_TOTAL_RATING                   //정렬방식
    var filterType = ArrayList<String>()                    //필터타입
    var filterHashTag = ArrayList<Int>()                    //해쉬태그타입
    var keyword: String? = null                             //검색키워드
    var filterTypeIsChecked = booleanArrayOf(false, false, false, false, false, false, false, false)
    var filterHashTagIsChecked =
        booleanArrayOf(false, false, false, false, false, false, false, false, false)

    var searchList = LinkedList<String>()                   //최근검색어


    init {
        selectedMajorListDefault.add("")
    }
    /* 학부 선택 최대 2개로 제한하는 함수 */
    fun isAddMajorPossible(): Boolean {
        return selectedMajorList.size < 2
    }
    /* 검색필터 지우는 함수 */
    fun filterClear(){
        selectedMajorList.clear()
        filterSort = SORT_BY_TOTAL_RATING
        filterType.clear()
        filterHashTag.clear()
        keyword = null
        Arrays.fill(filterTypeIsChecked, false)
        Arrays.fill(filterHashTagIsChecked, false)
        searchList.clear()
    }


    /* 강의평 목록을 가져오는 함수 */
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
    /* 강의평 개수를 가져오는 함수 */
    fun getLectureReviewCount(majors: ArrayList<String>){
        lectureRepository.getFilteredLectureList(majors,1,filterType, filterHashTag,filterSort, keyword)
            .withThread()
            .handleHttpException()
            .subscribe({
                _lectureListItemCount.value = it.count
            },{
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
            .addTo(compositeDisposable)
    }




}