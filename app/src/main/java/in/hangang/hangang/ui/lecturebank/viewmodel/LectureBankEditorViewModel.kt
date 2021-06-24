package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.timetable.Lecture
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo

class LectureBankEditorViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {

    private lateinit var title: String
    private lateinit var content: String
    private lateinit var category: String
    private lateinit var semesterDate: String

    private val semesterDateList = listOf(
        LECTURE_BANK_SEMESTER_DATE_ID_1,
        LECTURE_BANK_SEMESTER_DATE_ID_2,
        LECTURE_BANK_SEMESTER_DATE_ID_3,
        LECTURE_BANK_SEMESTER_DATE_ID_4,
        LECTURE_BANK_SEMESTER_DATE_ID_5
    )

    private val fileIsUploading = mutableMapOf<UploadFile, Disposable>()
    private val uploadedFile = mutableMapOf<UploadFile, String>()
    private val fileUploadStatusMap = mutableMapOf<UploadFile, Int>()
    private val uploadFileList = mutableListOf<UploadFile>()

    private val _lecture = MutableLiveData<Lecture?>()

    private val _uploadFiles = MutableLiveData<List<UploadFile>>()
    private val _fileUploadStatus = MutableLiveData<Pair<UploadFile, Int>>()
    private val _fileUploadError = MutableLiveData<Event<Pair<UploadFile, Throwable>>>()
    private val _lectureBankUploadRequested = MutableLiveData<Boolean>()
    private val _lectureBankUploaded = MutableLiveData<Event<CommonResponse>>()
    private val _lectureBankUploadError = MutableLiveData<Event<Throwable>>()

    val lecture: LiveData<Lecture?> get() = _lecture
    val uploadFiles: LiveData<List<UploadFile>> get() = _uploadFiles
    val fileUploadStatus: LiveData<Pair<UploadFile, Int>> get() = _fileUploadStatus
    val fileUploadError: LiveData<Event<Pair<UploadFile, Throwable>>> get() = _fileUploadError
    val lectureBankUploaded : LiveData<Event<CommonResponse>> get() = _lectureBankUploaded
    val lectureBankUploadError : LiveData<Event<Throwable>> get() = _lectureBankUploadError
    val lectureBankUploadRequested : LiveData<Boolean> get() = _lectureBankUploadRequested

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
                _lectureBankUploadRequested.postValue(false)
            }, {
                fileUploadStatusMap[uploadFile] = LectureBankFileAdapter.PROGRESS_NONE
                _fileUploadStatus.postValue(uploadFile to LectureBankFileAdapter.PROGRESS_NONE)
                fileIsUploading.remove(uploadFile)
                if(_lectureBankUploadRequested.value == true)
                    uploadLectureBankRx(title, content, category, semesterDate)
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

    fun uploadLectureBank(title: String, content: String, category: String, semesterDate: String) {
        if(fileIsUploading.isEmpty()) {
            uploadLectureBankRx(title, content, category, semesterDate)
        } else {
            _lectureBankUploadRequested.postValue(true)
        }
    }

    private fun uploadLectureBankRx(title: String, content: String, category: String, semesterDate: String) {
        _lectureBankUploadRequested.postValue(false)
        lectureBankRepository.uploadLectureBank(
            title, content, category, uploadedFile.values.toList(), _lecture.value?.id ?: -1, getLectureBankId(semesterDate)
        )
            .withThread()
            .handleHttpException()
            .handleProgress(this)
            .subscribe({
                _lectureBankUploaded.postValue(Event(it))
            }, {
                _lectureBankUploadError.postValue(Event(it))
            })
    }

    private fun getLectureBankId(semesterDate: String) : Int = semesterDateList.indexOf(semesterDate) + 1
}