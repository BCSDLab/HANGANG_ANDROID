package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.lecturebank.DownloadFileInfo
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.uploadfile.UploadFile
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.withThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LectureBankUploadFileViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {

    private val _downloadUrlEvent = MutableLiveData<Event<DownloadFileInfo>>()

    val downloadUrlEvent : LiveData<Event<DownloadFileInfo>> get() = _downloadUrlEvent

    fun getDownloadUrlFromUploadFile(uploadFile: UploadFile) {
        lectureBankRepository.downloadSingleFile(uploadFile.id)
            .withThread()
            .handleHttpException()
            .subscribe({
                       _downloadUrlEvent.value = Event(DownloadFileInfo(
                           uploadFile,
                           it
                       ))
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }

}