package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.data.entity.Lecture
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.data.uploadfile.UploadFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LectureBankEditorViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {

    private val _lecture = MutableLiveData<Lecture?>()

    private val _uploadFiles = MutableLiveData<List<UploadFile>>()
    private val _fileUploadStatus = MutableLiveData<Map<UploadFile, Int>>()

    val lecture : LiveData<Lecture?> get() = _lecture
    val uploadFiles : LiveData<List<UploadFile>> get() = _uploadFiles
    val fileUploadStatus : LiveData<Map<UploadFile, Int>> get() = _fileUploadStatus

    fun setSpinnerItem(lecture: Lecture) {

    }

    fun uploadFile() {

    }

    fun setLecture(lecture: Lecture?) {
        _lecture.postValue(lecture)
    }
}