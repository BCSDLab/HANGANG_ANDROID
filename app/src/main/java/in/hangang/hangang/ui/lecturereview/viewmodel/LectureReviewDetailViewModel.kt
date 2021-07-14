package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.constant.RECENTLY_READ_LECTURE_REVIEW
import `in`.hangang.hangang.data.entity.evaluation.*
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.entity.semester.Semester
import `in`.hangang.hangang.data.entity.timetable.TimeTable
import `in`.hangang.hangang.data.request.LectureEvaluationIdRequest
import `in`.hangang.hangang.data.request.LectureReviewReportRequest
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
import com.orhanobut.hawk.Hawk
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class LectureReviewDetailViewModel(
    private val lectureRepository: LectureRepository,
    private val timeTableRepository: TimeTableRepository
) :
    ViewModelBase() {
    private val _classLectureList = MutableLiveData<List<ClassLecture>>()
    val classLectureList: LiveData<List<ClassLecture>> get() = _classLectureList

    private val _chartList = MutableLiveData<ArrayList<BarEntry>>()
    val chartList: LiveData<ArrayList<BarEntry>> get() = _chartList

    private val _evaluationTotal = MutableLiveData<Evaluation>()
    val evaluationTotal: LiveData<Evaluation> get() = _evaluationTotal

    private val _recommendedDocs = MutableLiveData<ArrayList<LectureDoc>>()
    val recommendedDocs: LiveData<ArrayList<LectureDoc>> get() = _recommendedDocs

    private val _reviewList = MutableLiveData<PagingData<LectureReview>>()
    val reviewList: LiveData<PagingData<LectureReview>> get() = _reviewList

    private val _reviewCount = MutableLiveData<Int>()
    val reviewCount: LiveData<Int> get() = _reviewCount


    private val _userTimeTableList = MutableLiveData<List<TimeTable>>()
    val userTimeTableList: LiveData<List<TimeTable>> get() = _userTimeTableList

    private val _reportResult = MutableLiveData<CommonResponse>()
    val reportResult: LiveData<CommonResponse> get() = _reportResult

    private val _scrapResult = MutableLiveData<CommonResponse>()
    val scrapResult: LiveData<CommonResponse> get() = _scrapResult

    private val _semester = MutableLiveData<Semester>()
    val semester: LiveData<Semester> get() = _semester





    val SORT_BY_LIKE_COUNT = "좋아요 순"
    val SORT_BY_DATE_LATEST = "최신 순"
    var keyword: String? = null
    var sort: String = SORT_BY_LIKE_COUNT
    var commonResponse = MutableLiveData<CommonResponse>()
    //var lectureReviewItem = MutableLiveData<LectureReview>()

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
    fun getPersonalReviewCount(id:Int, keyword: String?, sort: String) {
        lectureRepository.getLectureReviewPersonalCount(id,keyword, sort)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _reviewCount.value = it.count
            }, {
                LogUtil.e(it.message.toString())
            })
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
/*
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


 */
    fun fetchDialogData(semesterId: Long, lectureId: Int) {
        var userTimeTableList = emptyList<TimeTable>()
        viewModelScope.launch {
            userTimeTableList = timeTableRepository.fetchTimeTables(semesterId) // 해당학기에 생성한 시간표 가져오기
            for(timetable in userTimeTableList) {
                val lectureList = timeTableRepository.fetchLectureListFromTimeTable(timetable.id).lectureList
                for(lecture in lectureList) {
                    var a = lecture
                    if(timetable.isChecked)
                        continue
                    else {
                        timetable.isChecked = lecture.lectureTimetableId == lectureId
                    }
                }

            }
            _userTimeTableList.postValue(userTimeTableList)
        }



    }

    fun fetchClassLectureList(id: Int, semesterId: Long) {

        var lectureList = emptyList<ClassLecture>()
        viewModelScope.launch {
            lectureList = lectureRepository.fetchClassLectures(id)
            var userTimeTableList = timeTableRepository.fetchTimeTables(semesterId) // 해당학기에 생성한 시간표 가져오기
            for(lecture in lectureList) {
                for (timetable in userTimeTableList) {
                    val userLectureList = timeTableRepository.fetchLectureListFromTimeTable(timetable.id).lectureList
                    for (userLecture in userLectureList) {
                        if(lecture.isChecked)
                            continue
                        else
                            lecture.isChecked = lecture.id == userLecture.lectureTimetableId
                    }

                }
            }
            _classLectureList.postValue(lectureList)
        }
    }
    fun addLectureInTimeTable(classLectureId: Int, timetableId: Int) {
        timeTableRepository.addLectureInTimeTable(classLectureId, timetableId)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                //commonResponse.value = it
            }, {
                LogUtil.e(it.message.toString())
            })
            .addTo(compositeDisposable)
    }
    fun deleteLectureInTimeTable(classLectureId: Int, timetableId: Int) {
        timeTableRepository.removeLectureFromTimeTable(classLectureId, timetableId)
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
    fun postScrap(id: Int) {
        var scrapedLecture: LectureEvaluationIdRequest = LectureEvaluationIdRequest(id)
        lectureRepository.postScrapedLecture(scrapedLecture)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _scrapResult.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage.toString())
            })
            .addTo(compositeDisposable)
    }
    fun deleteScrap(id: Int) {
        var scrapedLecture = ArrayList<Int>()
        scrapedLecture.add(id)

        lectureRepository.deleteScrapedLecture(scrapedLecture)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _scrapResult.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage.toString())
            })
            .addTo(compositeDisposable)
    }

    fun saveRecentlyReadLectureReviews(lecture: RankingLectureItem) {
        viewModelScope.launch {
            var isDuplicated = false
            var list = lectureRepository.getRecentlyLectureList()
            for(idx in list.indices) {
                if(list[idx].id == lecture.id){
                    isDuplicated = true
                    list.removeAt(idx)
                    list.add(0, lecture)
                    break
                }
            }
            if(!isDuplicated) {
                list.add(0, lecture)
                if (list.size > 5) {
                    list.removeLast()
                }
            }
            Hawk.put(RECENTLY_READ_LECTURE_REVIEW, list)
        }
    }
    fun getSemesterId(){
        timeTableRepository.getSemesterNow()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _semester.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage.toString())
            })
            .addTo(compositeDisposable)
    }
}