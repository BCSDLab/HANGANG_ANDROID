package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.evaluation.Chart
import `in`.hangang.hangang.data.evaluation.ClassLecture
import `in`.hangang.hangang.data.evaluation.Evaluation
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarEntry
import io.reactivex.rxjava3.kotlin.addTo

class LectureReviewDetailViewModel (private val lectureRepository: LectureRepository, private val timeTableRepository: TimeTableRepository) :
    ViewModelBase() {

    private val _classLectureList = MutableLiveData<ArrayList<ClassLecture>>()
    val classLectureList: LiveData<ArrayList<ClassLecture>> get() = _classLectureList

    private val _chartList = MutableLiveData<ArrayList<BarEntry>>()
    val chartList: LiveData<ArrayList<BarEntry>> get() = _chartList

    private val _evaluationTotal = MutableLiveData<Evaluation>()
    val evaluationTotal : LiveData<Evaluation> get() = _evaluationTotal

    var barEntryList: ArrayList<BarEntry> = ArrayList()

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
    fun getChartX(rating: Float): Float{
        return when(rating){
            in 0.1f..0.5f -> 1f
            in 0.6f..1f -> 2f
            in 1.1f..1.5f -> 3f
            in 1.6f..2f -> 4f
            in 2.1f..2.5f -> 5f
            in 2.6f..3f -> 6f
            in 3.1f..3.5f -> 7f
            in 3.6f..4f -> 8f
            in 4.1f..4.5f -> 9f
            in 4.6f..5f -> 10f
            else -> 0f
        }
    }
}