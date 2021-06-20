package `in`.hangang.hangang.ui.lecturebank.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.progressdialog.ProgressDialog
import `in`.hangang.core.util.getDisplayName
import `in`.hangang.core.util.getSize
import `in`.hangang.core.util.toProperCapacityUnit
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.timetable.Lecture
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.databinding.ActivityLectureBankEditorBinding
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankEditorActivityContract
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankEditorSelectLectureActivityContract
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankImagePickerContract
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankEditorViewModel
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.util.TypedValue
import android.webkit.MimeTypeMap
import android.widget.CheckBox
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureBankEditorActivity : ViewBindingActivity<ActivityLectureBankEditorBinding>() {
    override val layoutId = R.layout.activity_lecture_bank_editor

    private val lectureBankEditorViewModel: LectureBankEditorViewModel by viewModel()

    private val selectedCategory: CheckBox
        get() {
            categoryMap.keys.forEach {
                if (it.isChecked) return it
            }
            return binding.checkBoxLectureBankCategoryPreviousBank
        }
    private val categoryMap: Map<CheckBox, String> by lazy {
        mapOf(
            binding.checkBoxLectureBankCategoryPreviousBank to LECTURE_BANKS_CATEGORY_PREVIOUS,
            binding.checkBoxLectureBankCategoryWritingBank to LECTURE_BANKS_CATEGORY_WRITING,
            binding.checkBoxLectureBankCategoryAssignmentBank to LECTURE_BANKS_CATEGORY_ASSIGNMENT,
            binding.checkBoxLectureBankCategoryLectureBank to LECTURE_BANKS_CATEGORY_LECTURE,
            binding.checkBoxLectureBankCategoryEtcBank to LECTURE_BANKS_CATEGORY_ETC
        )
    }

    private val uploadRequestProgressDialog: ProgressDialog by lazy {
        ProgressDialog(
            this,
            getString(R.string.lecture_bank_pending_upload)
        )
    }

    private val lectureBankEditorSelectActivityContract = registerForActivityResult(
        LectureBankEditorSelectLectureActivityContract()
    ) {
        if (it.resultCode == RESULT_OK)
            lectureBankEditorViewModel.setLecture(it.lecture)
    }

    private val lectureBankEditorGetContentContract = registerForActivityResult(
        LectureBankImagePickerContract()
    ) {
        Log.d("Selected files", it.joinToString("\n"))
        it.forEach { uri ->
            lectureBankEditorViewModel.uploadSingleFile(
                uploadFile = UploadFile(
                    0, 0,
                    fileName = uri.getDisplayName(applicationContext) ?: "",
                    ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri)) ?: "*",
                    size = uri.getSize(applicationContext) ?: 0L,
                    url = ""
                ),
                uri = uri
            )
        }
    }

    private val lectureBankEditorUploadFileAdapter: LectureBankFileAdapter by lazy {
        LectureBankFileAdapter().apply {
            isEnabled = true
            isRemovable = true
            setOnItemClickListener { position, uploadFile ->
                lectureBankEditorUploadFileAdapter.removeUploadFile(position)
                lectureBankEditorViewModel.removeUploadFile(uploadFile)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()

        initWithLectureBank(intent.extras?.getParcelable(LectureBankEditorActivityContract.LECTURE_BANK))
    }

    private fun initWithLectureBank(lectureBank: LectureBank?) {
        with(binding) {
            if (lectureBank != null) {
                editTextLectureBankTitle.setText(lectureBank.title)
                setLectureTextView(lectureBank.lecture)
            }

            lectureBankEditorViewModel.setUploadFiles(lectureBank?.uploadFiles ?: emptyList())
        }
    }

    private fun initViewModel() {
        with(lectureBankEditorViewModel) {
            lecture.observe(this@LectureBankEditorActivity) {
                setLectureTextView(it)
                binding.spinnerLectureBankSemester.items = it?.semesters ?: emptyList()
            }
            uploadFiles.observe(this@LectureBankEditorActivity) {
                binding.recyclerViewListFiles.apply {
                    isVisible = !it.isNullOrEmpty()
                    lectureBankEditorUploadFileAdapter.setFiles(it)
                }
                binding.textViewPlzUploadFile.isVisible = it.isNullOrEmpty()
                binding.textViewFileCountCapacity.apply {
                    isVisible = !it.isNullOrEmpty()
                    text = this@LectureBankEditorActivity.getString(
                        R.string.lecture_bank_editor_file_info,
                        it.size,
                        it.sumOf { it.size }.toProperCapacityUnit(),
                        "50MB"
                    )
                }
                checkUploadAvailability()
            }
            fileUploadStatus.observe(this@LectureBankEditorActivity) {
                lectureBankEditorUploadFileAdapter.setDownloadStatus(
                    it.first,
                    it.second,
                    if (it.second >= 0) 100 else it.second
                )
            }
            lectureBankUploadRequested.observe(this@LectureBankEditorActivity) {
                if (it) uploadRequestProgressDialog.show()
                else uploadRequestProgressDialog.hide()
            }
            lectureBankUploaded.observe(this@LectureBankEditorActivity) {
                setResult(LectureBankEditorActivityContract.RESULT_UPLOADED)
                finish()
            }
            isLoading.observe(this@LectureBankEditorActivity) {
                if (it) showProgressDialog()
                else hideProgressDialog()
            }
        }
    }

    private fun initView() {
        binding.buttonLectureBankSearchLecture.setOnClickListener {
            lectureBankEditorSelectActivityContract.launch(lectureBankEditorViewModel.lecture.value)
        }

        binding.spinnerLectureBankSemester.spinnerLayout.background = null
        binding.textViewLectureBankSemester.setOnClickListener {
            binding.spinnerLectureBankSemester.callOnClick()
        }

        categoryMap.keys.forEach { checkBox ->
            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    categoryMap.keys.forEach { key -> key.isChecked = false }
                    checkBox.isChecked = true
                }
            }
        }

        binding.recyclerViewListFiles.apply {
            layoutManager = LinearLayoutManager(this@LectureBankEditorActivity, RecyclerView.HORIZONTAL, false)
            adapter = lectureBankEditorUploadFileAdapter
        }

        binding.buttonLectureBankNewImage.setOnClickListener {
            lectureBankEditorGetContentContract.launch(null)
        }

        binding.buttonLectureBankNewFinish.setOnClickListener {
            lectureBankEditorViewModel.uploadLectureBank(
                title = binding.editTextLectureBankTitle.text.toString(),
                content = binding.editTextLectureBankContent.text.toString(),
                semesterDate = binding.spinnerLectureBankSemester.selectedItem.toString(),
                category = selectedCategory.text.toString()
            )
        }
    }

    private fun setLectureTextView(lecture: Lecture?) {
        if (lecture == null) binding.textViewLectureBankLecture.text = ""
        else binding.textViewLectureBankLecture.text = SpannableStringBuilder()
            .inSpans(
                ForegroundColorSpan(
                    TypedValue().apply {
                        this@LectureBankEditorActivity.theme.resolveAttribute(
                            android.R.attr.textColorPrimary,
                            this,
                            true
                        )
                    }.data
                )
            ) {
                append(lecture.name)
                append(" ")
            }
            .inSpans(
                ForegroundColorSpan(
                    TypedValue().apply {
                        this@LectureBankEditorActivity.theme.resolveAttribute(
                            android.R.attr.textColorSecondary,
                            this,
                            true
                        )
                    }.data
                ),
                RelativeSizeSpan(12f / 14f)
            ) {
                append(lecture.professor)
            }
    }

    private fun checkUploadAvailability() {
        binding.buttonLectureBankNewFinish.isEnabled =
            binding.editTextLectureBankTitle.text.isNotBlank() &&
                    binding.editTextLectureBankContent.text.isNotBlank() &&
                    binding.spinnerLectureBankSemester.items.isNotEmpty()
    }
}