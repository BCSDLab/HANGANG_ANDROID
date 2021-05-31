package `in`.hangang.hangang.ui.lecturereview.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.evaluation.ClassLecture
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class LectureEvaluationViewModel(private val lectureRepository: LectureRepository): ViewModelBase() {
    private val _semesterList = MutableLiveData<ArrayList<String>>()
    val semesterLectureList: LiveData<ArrayList<String>> get() = _semesterList

    fun getLectureSemester(id: Int){
        lectureRepository.getLectureSemester(id)
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _semesterList.value = it
            },{
                LogUtil.e("Error : ${it.toCommonResponse().errorMessage}")
            })
            .addTo(compositeDisposable)
    }

}