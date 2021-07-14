package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.lecturebank.DownloadFileInfo
import `in`.hangang.hangang.data.response.toCommonResponse
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.withThread
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import java.lang.Exception

class LectureBankUploadFileViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {

    private val _downloadUrlEvent = MutableLiveData<Event<DownloadFileInfo>>()
    private val _openFileEvent = MutableLiveData<Event<Uri>>()

    val downloadUrlEvent: LiveData<Event<DownloadFileInfo>> get() = _downloadUrlEvent
    val openFileEvent: LiveData<Event<Uri>> get() = _openFileEvent

    fun downloadOrOpenFile(context: Context, uploadFile: UploadFile) {
        Hawk.get<String?>(uploadFile.id.toString(), null)?.let { stringUri ->
            Uri.parse(stringUri)?.let { uri ->
                try {
                    context.contentResolver.openFileDescriptor(uri, "r").use {
                        if(it?.fileDescriptor?.valid() == true) {
                            _openFileEvent.postValue(Event(uri))
                        } else {
                            downloadFile(uploadFile)
                        }
                    }
                } catch (e: Exception) {
                    downloadFile(uploadFile)
                }
            } ?: downloadFile(uploadFile)
        } ?: downloadFile(uploadFile)
    }

    private fun downloadFile(uploadFile: UploadFile) {
        Hawk.delete(uploadFile.id.toString())
        lectureBankRepository.downloadSingleFile(uploadFile.id)
            .withThread()
            .handleHttpException()
            .subscribe({
                _downloadUrlEvent.value = Event(
                    DownloadFileInfo(uploadFile, it)
                )
            }, {
                LogUtil.e(it.toCommonResponse().errorMessage)
            })
    }

}