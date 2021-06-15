package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.data.entity.timetable.Lecture
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo

class LectureBankEditorViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {

    private val fileIsUploading = mutableMapOf<UploadFile, Disposable>()
    private val uploadedFile = mutableMapOf<UploadFile, String>()
    private val fileUploadStatusMap = mutableMapOf<UploadFile, Int>()
    private val uploadFileList = mutableListOf<UploadFile>()

    private val _lecture = MutableLiveData<Lecture?>()

    private val _uploadFiles = MutableLiveData<List<UploadFile>>()
    private val _fileUploadStatus = MutableLiveData<Pair<UploadFile, Int>>()
    private val _fileUploadError = MutableLiveData<Event<Pair<UploadFile, Throwable>>>()

    val lecture: LiveData<Lecture?> get() = _lecture
    val uploadFiles: LiveData<List<UploadFile>> get() = _uploadFiles
    val fileUploadStatus: LiveData<Pair<UploadFile, Int>> get() = _fileUploadStatus
    val fileUploadError: LiveData<Event<Pair<UploadFile, Throwable>>> get() = _fileUploadError

    fun uploadSingleFile(uploadFile: UploadFile, uri: Uri) {
        fileUploadStatusMap[uploadFile] = -1
        uploadFileList.add(uploadFile)
        _uploadFiles.postValue(uploadFileList)
        fileIsUploading[uploadFile] = lectureBankRepository.uploadSingleFile(uri)
            .subscribe({
                fileUploadStatusMap[uploadFile] = it.percentage
                _fileUploadStatus.postValue(uploadFile to it.percentage)
                if (it.response != null)
                    uploadedFile[uploadFile] = it.response!!
            }, {
                _fileUploadError.postValue(Event(uploadFile to it))
                fileIsUploading.remove(uploadFile)
            }, {
                fileUploadStatusMap[uploadFile] = LectureBankFileAdapter.PROGRESS_NONE
                _fileUploadStatus.postValue(uploadFile to LectureBankFileAdapter.PROGRESS_NONE)
                fileIsUploading.remove(uploadFile)
            })
            .addTo(compositeDisposable)
    }

    fun setUploadFiles(uploadFiles: List<UploadFile>) {
        _uploadFiles.postValue(uploadFiles)
    }

    fun setLecture(lecture: Lecture?) {
        _lecture.postValue(lecture)
    }

    fun removeUploadFile(uploadFile: UploadFile) {
        fileIsUploading[uploadFile]?.dispose()
        fileIsUploading.remove(uploadFile)
        uploadedFile.remove(uploadFile)
        uploadFileList.remove(uploadFile)
        fileUploadStatusMap.remove(uploadFile)

        _uploadFiles.postValue(uploadFileList)
    }
}