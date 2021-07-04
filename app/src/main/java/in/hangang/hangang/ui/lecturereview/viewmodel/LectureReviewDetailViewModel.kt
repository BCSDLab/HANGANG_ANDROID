package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.evaluation.ClassLecture
import `in`.hangang.hangang.data.entity.evaluation.Evaluation
import `in`.hangang.hangang.data.entity.evaluation.LectureDoc
import `in`.hangang.hangang.data.entity.evaluation.LectureReview
import `in`.hangang.hangang.data.request.ReviewRecommendRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.LectureRepository
import `in`.hangang.hangang.data.source.repository.TimeTableRepository
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
import io.reactivex.rxjava3.kotlin.addTo

class LectureReviewDetailViewModel (private val lectureRepository: LectureRepository, private val timeTableRepository: TimeTableRepository) :
    ViewModelBase() {
    val REVIEW_RECOMMEND_KEY = "id"
    private val _classLectureList = MutableLiveData<ArrayList<ClassLecture>>()
    val classLectureList: LiveData<ArrayList<ClassLecture>> get() = _classLectureList

    private val _chartList = MutableLiveData<ArrayList<BarEntry>>()
    val chartList: LiveData<ArrayList<BarEntry>> get() = _chartList

    private val _evaluationTotal = MutableLiveData<Evaluation>()
    val evaluationTotal : LiveData<Evaluation> get() = _evaluationTotal

    private val _recommendedDocs = MutableLiveData<ArrayList<LectureDoc>>()
    val recommendedDocs : LiveData<ArrayList<LectureDoc>> get() = _recommendedDocs

    private val _reviewList = MutableLiveData<PagingData<LectureReview>>()
    val reviewList : LiveData<PagingData<LectureReview>> get() = _reviewList

    var keyword: String? = null
    var sort = "좋아요"
    var commonResponse = MutableLiveData<CommonResponse>()
    var lectureReviewItem = MutableLiveData<LectureReview>()

    fun getReviewList(id: Int, keyword: String?, sort: String){
        lectureRepository.getLectureReviewList(id,keyword,sort)
            .cachedIn(viewModelScope)
            .subscribe {
                _reviewList.value = it
            }
            .addTo(compositeDisposable)
    }

    fun getRecommentedDocs(keyword: String){
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

    fun getEvaluationRating(id: Int){
        lectureRepository.getEvaluationRating(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .map { it -> getBarEntryList(it) }
            .subscribe({
                _chartList.value = it
            },{
                LogUtil.e("Error : ${it.toCommonResponse().errorMessage}")
            })
            .addTo(compositeDisposable)
    }
    fun getEvaluationTotal(id: Int){
        lectureRepository.getEvaluationTotal(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _evaluationTotal.value = it
            },{
                LogUtil.e("Error : ${it.toCommonResponse().errorMessage}")
            })
            .addTo(compositeDisposable)
    }
    fun getClassLectureList(id: Int){
        lectureRepository.getClassLecture(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _classLectureList.value = it
            },{
                LogUtil.e(it.message.toString())
            })
            .addTo(compositeDisposable)
    }
    fun getUserTimeTableList(){
    }
    fun getBarEntryList(list: ArrayList<Int>): ArrayList<BarEntry>{
        var barEntryList = ArrayList<BarEntry>()
        for(i in 0..9 ){
            var x = (i+1).toFloat()
            var y = list.get(i).toFloat()
            var barEntry = BarEntry(x,y)
            barEntryList.add(barEntry)
        }
        return barEntryList
    }
    fun postReviewRecommend(reviewId: Int){
        var reviewRecommendRequest = ReviewRecommendRequest(reviewId)
        lectureRepository.postReviewRecommend(reviewRecommendRequest)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                commonResponse.value = it
            },{
                LogUtil.e(it.message.toString())
            })
            .addTo(compositeDisposable)
    }
    fun getReviewLectureItem(id: Int){
        lectureRepository.getLectureReviewItem(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                lectureReviewItem.value = it
            },{
                LogUtil.e(it.message.toString())
            })
            .addTo(compositeDisposable)
    }
}