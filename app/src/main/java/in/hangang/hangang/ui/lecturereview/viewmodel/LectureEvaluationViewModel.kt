package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.evaluation.ClassLecture
import `in`.hangang.hangang.data.request.LectureEvaluationIdRequest
import `in`.hangang.hangang.data.request.LectureEvaluationRequest
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.util.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class LectureEvaluationViewModel(private val lectureRepository: LectureRepository): ViewModelBase() {
    private val _semesterList = MutableLiveData<ArrayList<String>>()
    val semesterLectureList: LiveData<ArrayList<String>> get() = _semesterList
    var semesterData = ArrayList<Int>()


    var assignment = ArrayList<Int>()
    var assignmentAmount = 3
    var attendanceFrequency = 3
    var comment = ""
    var difficulty = 3
    var gradePortion = 3
    var hashTag = ArrayList<Int>()
    var lectureId = 0
    var rating = 0.5f
    var semesterId = 5

    var postCommonResponse = MutableLiveData<CommonResponse>()

    fun getLectureSemester(id: Int){
        lectureRepository.getLectureSemester(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                semesterData.addAll(it)
                semesterId = semesterData.first()
                _semesterList.value = semesterToString(it)
            },{
                LogUtil.e("Error : ${it.toCommonResponse().errorMessage}")
            })
            .addTo(compositeDisposable)
    }
    fun semesterToString(list: ArrayList<Int>): ArrayList<String>{
        var semesterList = ArrayList<String>()
        for(i in list){
            semesterList.add(ClassLectureTimeUtil.getSemester(i))
        }
        return semesterList


    }
    fun postEvaluation(){
        var assignmentList = ArrayList<LectureEvaluationIdRequest>()
        var hashtagList = ArrayList<LectureEvaluationIdRequest>()
        for(i in assignment){
            LogUtil.e("assignment ${i}")
            assignmentList.add(LectureEvaluationIdRequest(i))
        }
        for(i in hashTag){
            LogUtil.e("assignment ${i}")
            hashtagList.add(LectureEvaluationIdRequest(i))
        }
        LogUtil.e("assignmentAmount ${assignmentAmount}")
        LogUtil.e("attendanceFrequency ${attendanceFrequency}")
        LogUtil.e("comment ${comment}")
        LogUtil.e("difficulty ${difficulty}")
        LogUtil.e("gradePortion ${gradePortion}")
        LogUtil.e("lectureId ${lectureId}")
        LogUtil.e("rating ${rating}")
        LogUtil.e("semesterId ${semesterId}")
        var lectureEvaluationRequest = LectureEvaluationRequest(assignmentList, assignmentAmount,attendanceFrequency,comment, difficulty, gradePortion, hashtagList,
        lectureId, rating, semesterId)
        lectureRepository.postEvaluation(lectureEvaluationRequest)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                       postCommonResponse.value = it
            },{
                LogUtil.e(it.message)
                //LogUtil.e("Error : ${it.toCommonResponse().errorMessage}")
            })
            .addTo(compositeDisposable)
    }

}