package `in`.hangang.hangang.ui.lecturebank.viewmodel

import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.core.livedata.Event
import `in`.hangang.core.util.getDisplayName
import `in`.hangang.core.util.getSize
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.data.source.repository.LectureBankRepository
import `in`.hangang.hangang.util.handleHttpException
import `in`.hangang.hangang.util.withThread
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo

class LectureBankEditorViewModel(
    private val lectureBankRepository: LectureBankRepository
) : ViewModelBase() {

    private var uploadDisposable : Disposable? = null

    private val semesterDateList = listOf(
        SEMESTER_DATE_ID_1,
        SEMESTER_DATE_ID_2,
        SEMESTER_DATE_ID_3,
        SEMESTER_DATE_ID_4,
        SEMESTER_DATE_ID_5,
        SEMESTER_DATE_ID_6,
        SEMESTER_DATE_ID_7,
        SEMESTER_DATE_ID_8,
        SEMESTER_DATE_ID_9,
    )

    private val uploadFileMap = mutableMapOf<UploadFile, Uri>()

    private val _lecture = MutableLiveData<Lecture?>()

    private val _uploadFiles = MutableLiveData<Map<UploadFile, Uri>>()
    private val _lectureBankUploadProgress =
        MutableLiveData(LectureBankUploadStatus.LECTURE_BANK_NOT_UPLOADING to 0) //status, progress pair
    private val _lectureBankUploadError = MutableLiveData<Event<Throwable>>()

    val lecture: LiveData<Lecture?> get() = _lecture
    val uploadFiles: LiveData<Map<UploadFile, Uri>> get() = _uploadFiles
    val lectureBankUploadProgress: LiveData<Pair<LectureBankUploadStatus, Int>> get() = _lectureBankUploadProgress //status, progress pair
    val lectureBankUploadError: LiveData<Event<Throwable>> get() = _lectureBankUploadError

    fun uploadFiles(applicationContext: Context, uris: List<Uri>) {
        uris.forEach { uri ->
            val uploadFile = UploadFile(
                0, 0,
                fileName = uri.getDisplayName(applicationContext) ?: "",
                ext = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(applicationContext.contentResolver.getType(uri))
                    ?: "*",
                size = uri.getSize(applicationContext) ?: 0L,
                url = ""
            )

            uploadFileMap[uploadFile] = uri
        }

        _uploadFiles.postValue(uploadFileMap)
    }

    fun setLecture(lecture: Lecture?) {
        _lecture.postValue(lecture)
    }

    fun removeUploadFile(uploadFile: UploadFile) {
        uploadFileMap.remove(uploadFile)

        _uploadFiles.postValue(uploadFileMap)
    }

    fun uploadLectureBank(title: String, content: String, category: String, semesterDate: String) {
        val uploadedFile = mutableListOf<String>()
        val uploadFileCount = uploadFileMap.size
        var totalProgress = 0

        if (uploadFileCount > 0) {
            _lectureBankUploadProgress.postValue(
                LectureBankUploadStatus.LECTURE_BANK_UPLOADING_FILES to totalProgress / uploadFileCount
            )

            uploadDisposable = Observable.concat(uploadFileMap.values.map { lectureBankRepository.uploadSingleFile(it) })
                .withThread()
                .handleHttpException()
                .subscribe({
                    totalProgress += it.percentage
                    it.response?.let { tempUrl ->
                        uploadedFile.add(tempUrl)
                    }

                    _lectureBankUploadProgress.postValue(
                        LectureBankUploadStatus.LECTURE_BANK_UPLOADING_FILES to totalProgress / uploadFileCount
                    )
                }, {
                    _lectureBankUploadError.postValue(Event(it))
                }, {
                    _lectureBankUploadProgress.value =
                        LectureBankUploadStatus.LECTURE_BANK_UPLOADING_LECTURE_BANK to 100

                    lectureBankRepository.uploadLectureBank(
                        title,
                        content,
                        category,
                        uploadedFile,
                        _lecture.value?.id ?: -1,
                        getLectureBankId(semesterDate)
                    )
                        .withThread()
                        .handleHttpException()
                        .subscribe({
                            _lectureBankUploadProgress.value =
                                LectureBankUploadStatus.LECTURE_BANK_UPLOADED to 100
                        }, {
                            _lectureBankUploadError.postValue(Event(it))
                        })
                })
                .addTo(compositeDisposable)
        }
    }

    fun cancelUploadLectureBank() {
        uploadDisposable?.dispose()
        _lectureBankUploadProgress.postValue(LectureBankUploadStatus.LECTURE_BANK_NOT_UPLOADING to 0)
    }

    private fun getLectureBankId(semesterDate: String): Int =
        semesterDateList.indexOf(semesterDate) + 1

    enum class LectureBankUploadStatus {
        LECTURE_BANK_NOT_UPLOADING,
        LECTURE_BANK_UPLOADING_FILES,
        LECTURE_BANK_UPLOADING_LECTURE_BANK,
        LECTURE_BANK_UPLOADED
    }
}