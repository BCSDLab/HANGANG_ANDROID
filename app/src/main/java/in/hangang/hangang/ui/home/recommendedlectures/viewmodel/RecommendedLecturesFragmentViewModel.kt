package `in`.hangang.hangang.ui.home.recommendedlectures.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.evaluation.LectureDoc
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.UserRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.kotlin.addTo

class RecommendedLecturesFragmentViewModel(private val userRepository: UserRepository) : ViewModelBase() {
    private val _recommendedLectureDocs = MutableLiveData<List<LectureDoc>>()
    val recommendedLectureDocs: LiveData<List<LectureDoc>> get() = _recommendedLectureDocs

    fun getLectureBankHit() {
        userRepository.getLectureBankHit()
            .handleHttpException()
            .handleProgress(this)
            .withThread()
            .subscribe({
                _recommendedLectureDocs.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage.toString())
            })
            .addTo(compositeDisposable)
    }
}