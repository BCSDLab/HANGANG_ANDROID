package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.data.uploadfile.UploadFile
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import `in`.hangang.hangang.util.withThread
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File

class LectureBankEditorViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {

    private val fileIsUploading = mutableSetOf<UploadFile>()
    private val uploadedFile = mutableSetOf<Pair<UploadFile, String>>()
    private val fileUploadStatusMap = mutableMapOf<UploadFile, Int>()

    private val _lecture = MutableLiveData<Lecture?>()

    private val _uploadFiles = MutableLiveData<List<UploadFile>>()
    private val _fileUploadStatus = MutableLiveData<Map<UploadFile, Int>>()
    private val _fileUploadError = MutableLiveData<Event<Pair<UploadFile, Throwable>>>()

    val lecture: LiveData<Lecture?> get() = _lecture
    val uploadFiles: LiveData<List<UploadFile>> get() = _uploadFiles
    val fileUploadStatus: LiveData<Map<UploadFile, Int>> get() = _fileUploadStatus
    val fileUploadError: LiveData<Event<Pair<UploadFile, Throwable>>> get() = _fileUploadError

    fun uploadSingleFile(uploadFile: UploadFile, uri: Uri, contentType: String) {
        fileIsUploading.add(uploadFile)
        _uploadFiles.postValue((_uploadFiles.value ?: listOf()).toMutableList().apply { add(uploadFile) })
        lectureBankRepository.uploadSingleFile(uri)
            .withThread()
            .subscribe({
                fileUploadStatusMap[uploadFile] = it.percentage
                _fileUploadStatus.postValue(fileUploadStatusMap)
                if (it.response != null)
                    uploadedFile.add(uploadFile to it.response!!)
            }, {
                _fileUploadError.postValue(Event(uploadFile to it))
                fileIsUploading.remove(uploadFile)
            }, {
                fileUploadStatusMap[uploadFile] = LectureBankFileAdapter.PROGRESS_NONE
            })
    }

    fun setUploadFiles(uploadFiles: List<UploadFile>) {
        _uploadFiles.postValue(uploadFiles)
    }

    fun setLecture(lecture: Lecture?) {
        _lecture.postValue(lecture)
    }
}