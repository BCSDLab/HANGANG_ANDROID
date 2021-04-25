package `in`.hangang.hangang.ui.mypage.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.source.repository.LectureRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single

class MyScrapViewModel(
    private val lectureRepository: LectureRepository
) : ViewModelBase() {

    private val _myScrapLecture = MutableLiveData<List<Lecture>>()
    private val _isEditMode = MutableLiveData<Boolean>()

    val myScrapLecture : LiveData<List<Lecture>> get() = _myScrapLecture
    val isEditMode : LiveData<Boolean> get() = _isEditMode

    fun getMyScrapLecture() {
        lectureRepository.getScrapedLecture()
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _myScrapLecture.value = it
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }

    fun unscrapLecture(vararg lectures: Lecture) {
        Single.zip(
            lectures.map { lectureRepository.unscrapLecture(it.id) }
        ) {
            lectureRepository.getScrapedLecture()
                .doOnSuccess {
                    _myScrapLecture.postValue(it)
                    _isEditMode.postValue(false)
                }

            val success = mutableListOf<CommonResponse>()
            val throwable = mutableListOf<Throwable>()
            it.forEach { result ->
                if(result is CommonResponse) success.add(result)
                else if(result is Throwable) throwable.add(result)
            }
            success to throwable
        }
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                if(!it.second.isEmpty()) {
                    //Partially error when removing scrap lecture
                    LogUtil.e("Partially error when removing scrap lecture")
                }
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }

    fun setEditMode(isEdit : Boolean) {
        _isEditMode.postValue(isEdit)
    }
}