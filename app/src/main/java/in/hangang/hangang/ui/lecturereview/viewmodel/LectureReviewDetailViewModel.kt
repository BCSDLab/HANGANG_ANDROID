package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.data.entity.TimeTableWithLecture
import `in`.hangang.hangang.data.evaluation.*
import `in`.hangang.hangang.data.request.LectureReviewReportRequest
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.di.repositoryModule
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.github.mikephil.charting.data.BarEntry
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LectureReviewDetailViewModel(
    private val lectureRepository: LectureRepository,
    private val timeTableRepository: TimeTableRepository
) :
    ViewModelBase() {
    private val _classLectureList = MutableLiveData<ArrayList<ClassLecture>>()
    val classLectureList: LiveData<ArrayList<ClassLecture>> get() = _classLectureList

    private val _chartList = MutableLiveData<ArrayList<BarEntry>>()
    val chartList: LiveData<ArrayList<BarEntry>> get() = _chartList

    private val _evaluationTotal = MutableLiveData<Evaluation>()
    val evaluationTotal: LiveData<Evaluation> get() = _evaluationTotal

    private val _recommendedDocs = MutableLiveData<ArrayList<LectureDoc>>()
    val recommendedDocs: LiveData<ArrayList<LectureDoc>> get() = _recommendedDocs

    private val _reviewList = MutableLiveData<PagingData<LectureReview>>()
    val reviewList: LiveData<PagingData<LectureReview>> get() = _reviewList

    private val _userTimeTableList = MutableLiveData<List<TimeTable>>()
    val userTimeTableList: LiveData<List<TimeTable>> get() = _userTimeTableList

    private val _reportResult = MutableLiveData<CommonResponse>()
    val reportResult: LiveData<CommonResponse> get() = _reportResult

    private val _timeTableWithLectureList = MutableLiveData<TimeTableWithLecture>()
    val timeTableWithLectureList: LiveData<TimeTableWithLecture> get() = _timeTableWithLectureList

    private val _classWithLecture = MutableLiveData<ArrayList<Boolean>>()
    val classWithLecture: LiveData<ArrayList<Boolean>> get() = _classWithLecture


    val classWithTimeTableList = HashMap<Int, List<LectureTimeTable>>()



    val SORT_BY_LIKE_COUNT = "좋아요 순"
    val SORT_BY_DATE_LATEST = "최신 순"
    var keyword: String? = null
    var sort: String = SORT_BY_LIKE_COUNT
    var commonResponse = MutableLiveData<CommonResponse>()
    var lectureReviewItem = MutableLiveData<LectureReview>()

    fun reportLectureReview(lectureReviewReportRequest: LectureReviewReportRequest) {
        lectureRepository.reportLectureReview(lectureReviewReportRequest)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _reportResult.value = it
            }, { LogUtil.e(it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun getReviewList(id: Int, keyword: String?, sort: String) {
        lectureRepository.getLectureReviewList(id, keyword, sort)
            .cachedIn(viewModelScope)
            .subscribe {
                _reviewList.value = it
            }
            .addTo(compositeDisposable)
    }

    fun getRecommentedDocs(keyword: String) {
        lectureRepository.getRecommentedDocs(keyword)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _recommendedDocs.value = it.result
            }, {
                LogUtil.e(it.message.toString())
            })
    }

    fun getEvaluationRating(id: Int) {
        lectureRepository.getEvaluationRating(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .map { it -> getBarEntryList(it) }
            .subscribe({
                _chartList.value = it
            }, {
                LogUtil.e("Error : ${it.toCommonResponse().errorMessage}")
            })
            .addTo(compositeDisposable)
    }

    fun getEvaluationTotal(id: Int) {
        lectureRepository.getEvaluationTotal(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _evaluationTotal.value = it
            }, {
                LogUtil.e("Error : ${it.toCommonResponse().errorMessage}")
            })
            .addTo(compositeDisposable)
    }

    fun getClassLectureList(id: Int) {
        lectureRepository.getClassLecture(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _classLectureList.value = it
            }, {
                LogUtil.e(it.message.toString())
            })
            .addTo(compositeDisposable)
    }

    fun getBarEntryList(list: ArrayList<Int>): ArrayList<BarEntry> {
        var barEntryList = ArrayList<BarEntry>()
        for (i in 0..9) {
            var x = (i + 1).toFloat()
            var y = list.get(i).toFloat()
            var barEntry = BarEntry(x, y)
            barEntryList.add(barEntry)
        }
        return barEntryList
    }

    fun postReviewRecommend(reviewId: Int) {
        var reviewRecommendRequest = ReviewRecommendRequest(reviewId)
        lectureRepository.postReviewRecommend(reviewRecommendRequest)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                commonResponse.value = it
            }, {
                LogUtil.e(it.message.toString())
            })
            .addTo(compositeDisposable)
    }

    fun getReviewLectureItem(id: Int) {
        lectureRepository.getLectureReviewItem(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                lectureReviewItem.value = it
            }, {
                LogUtil.e(it.message.toString())
            })
            .addTo(compositeDisposable)
    }

    fun getUserTimeTables(semesterId: Long) {
        timeTableRepository.getUserTimeTables(semesterId)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _userTimeTableList.value = it
            }, {
                LogUtil.e(it.message.toString())
            })
            .addTo(compositeDisposable)
    }

    fun getTimetable() {

        for(id in _userTimeTableList.value!!) {
            timeTableRepository.getTimetable(id.id)
                .handleHttpException()
                .handleProgress(this)
                .withThread()
                .subscribe({
                    classWithTimeTableList.put(it.id, it.lectureList)
                    checkClassAndTimeTable()
                }, {
                    LogUtil.e(it.message.toString())
                })
                .addTo(compositeDisposable)

        }

    }
    fun setDialogCheckButton(id: Int){
        val list = ArrayList<TimeTable>()
        _userTimeTableList.value?.let { list.addAll(it) }
        var keys = classWithTimeTableList.keys
        var idx = 0
        for(key in keys){
            val target = classWithTimeTableList.get(key)
            if(target != null){
                for(targetId in target){
                    if(targetId.lectureId == id) {
                        list[idx].isChecked = true
                        break
                    } else {
                        list[idx].isChecked = false
                    }
                }
            }
            idx++
        }
        _userTimeTableList.value = list
    }
    fun checkClassAndTimeTable(){
        val list = ArrayList<Boolean>()
        for(classId in _classLectureList.value!!){
            for(key in classWithTimeTableList.keys) {
                val target = classWithTimeTableList.get(key)
                if (target != null) {
                    for(targetId in target){
                        if (targetId.lectureId == classId.id){
                            list.add(true)
                        } else {
                            list.add(false)
                        }
                    }
                }
            }

        }
        _classWithLecture.value = list
    }
}