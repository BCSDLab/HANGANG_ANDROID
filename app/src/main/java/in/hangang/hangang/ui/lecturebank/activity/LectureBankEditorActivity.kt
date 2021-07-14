package `in`.hangang.hangang.ui.lecturebank.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.util.getSize
import `in`.hangang.core.util.toProperCapacityUnit
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.lecture.Lecture
import `in`.hangang.hangang.data.entity.lecturebank.LectureBank
import `in`.hangang.hangang.databinding.ActivityLectureBankEditorBinding
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankEditorActivityContract
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankEditorSelectLectureActivityContract
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankFilePickerContract
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankImagePickerContract
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankEditorViewModel
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankEditorViewModel.LectureBankUploadStatus.*
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureBankEditorActivity : ViewBindingActivity<ActivityLectureBankEditorBinding>() {
    override val layoutId = R.layout.activity_lecture_bank_editor

    private val lectureBankEditorViewModel: LectureBankEditorViewModel by viewModel()
    private var lectureBankUploadProgressDialog: Dialog? = null
    private var lectureBankUploadProgressView: View? = null

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

    private val lectureBankEditorSelectLectureActivityContract = registerForActivityResult(
        LectureBankEditorSelectLectureActivityContract()
    ) {
        if (it.resultCode == RESULT_OK) {
            lectureBankEditorViewModel.setLecture(it.lecture)
        }
    }

    private val lectureBankEditorImagePickerContract = registerForActivityResult(
        LectureBankImagePickerContract(), this::uploadSelectedUris
    )

    private val lectureBankEditorFilePickerContract = registerForActivityResult(
        LectureBankFilePickerContract(), this::uploadSelectedUris
    )

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

    override fun onBackPressed() {
        showConfirmExitDialog()
    }

    private fun showConfirmExitDialog() {
        DialogUtil.makeSimpleDialog(
            this,
            message = getString(R.string.lecture_bank_editor_exit_message),
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { _, _ -> super.onBackPressed() },
            negativeButtonText = getString(R.string.close),
            negativeButtonOnClickListener = { dialog, _ -> dialog.dismiss() },
            cancelable = true
        ).show()
    }

    private fun initWithLectureBank(lectureBank: LectureBank?) {
        with(binding) {
            if (lectureBank != null) {
                editTextLectureBankTitle.setText(lectureBank.title)
                setLectureTextView(lectureBank.lecture)
            }
            checkUploadAvailability()
        }
    }

    private fun initViewModel() {
        with(lectureBankEditorViewModel) {
            lecture.observe(this@LectureBankEditorActivity) {
                setLectureTextView(it)
                binding.spinnerLectureBankSemester.items = it?.semesters ?: emptyList()
                checkUploadAvailability()
            }
            uploadFiles.observe(this@LectureBankEditorActivity) {
                binding.recyclerViewListFiles.apply {
                    isVisible = !it.isNullOrEmpty()
                    lectureBankEditorUploadFileAdapter.setFiles(it.keys)
                }
                binding.textViewPlzUploadFile.isVisible = it.isNullOrEmpty()
                binding.textViewFileCountCapacity.apply {
                    isVisible = !it.isNullOrEmpty()
                    text = this@LectureBankEditorActivity.getString(
                        R.string.lecture_bank_editor_file_info,
                        it.size,
                        it.keys.sumOf { it.size }.toProperCapacityUnit(),
                        "50MB"
                    )
                }
                checkUploadAvailability()
            }
            lectureBankUploadProgress.observe(this@LectureBankEditorActivity) {
                when (it.first) {
                    LECTURE_BANK_NOT_UPLOADING -> {
                        lectureBankUploadProgressDialog?.dismiss()
                        lectureBankUploadProgressDialog = null
                    }
                    LECTURE_BANK_UPLOADING_FILES -> {
                        setLectureBankProgressDialog(
                            getString(R.string.lecture_bank_uploading_files),
                            it.second,
                            false
                        )
                    }
                    LECTURE_BANK_UPLOADING_LECTURE_BANK -> {
                        setLectureBankProgressDialog(
                            getString(R.string.lecture_bank_uploading_lecture_bank),
                            100,
                            true
                        )
                    }
                    LECTURE_BANK_UPLOADED -> {
                        setResult(LectureBankEditorActivityContract.RESULT_UPLOADED)
                        finish()
                    }
                }
            }
            lectureBankUploadError.observe(this@LectureBankEditorActivity) {
                showLectureBankUploadErrorDialog()
            }
            isLoading.observe(this@LectureBankEditorActivity) {
                if (it) showProgressDialog()
                else hideProgressDialog()
            }
        }
    }

    private fun showLectureBankUploadErrorDialog() {
        DialogUtil.makeSimpleDialog(
            this,
            message = getString(R.string.lecture_bank_upload_error_message),
            positiveButtonText = getString(R.string.lecture_bank_upload_retry),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
                lectureBankEditorViewModel.uploadLectureBank(
                    title = binding.editTextLectureBankTitle.text.toString(),
                    content = binding.editTextLectureBankContent.text.toString(),
                    semesterDate = binding.spinnerLectureBankSemester.selectedItem.toString(),
                    category = selectedCategory.text.toString()
                )
            },
            negativeButtonText = getString(R.string.cancel),
            negativeButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            }
        ).show()
    }

    private fun setLectureBankProgressDialog(
        message: String,
        progress: Int,
        indeterminate: Boolean
    ) {
        if (lectureBankUploadProgressDialog == null) {
            lectureBankUploadProgressView =
                LayoutInflater.from(this).inflate(R.layout.lecture_bank_progress_dialog, null)
            lectureBankUploadProgressDialog = DialogUtil.makeViewDialog(
                context = this,
                view = lectureBankUploadProgressView!!,
                positiveButtonText = getString(R.string.cancel),
                positiveButtonOnClickListener = { dialog, _ ->
                    lectureBankEditorViewModel.cancelUploadLectureBank()
                },
                cancelable = false
            )
        }

        lectureBankUploadProgressView?.findViewById<TextView>(R.id.text_view_message)?.text =
            message
        lectureBankUploadProgressView?.findViewById<ProgressBar>(R.id.progress_bar_upload)?.apply {
            this.progress = progress
            this.isIndeterminate = indeterminate
        }

        lectureBankUploadProgressDialog?.show()
    }

    private fun initView() {
        binding.editTextLectureBankTitle.addTextChangedListener {
            checkUploadAvailability()
        }
        binding.editTextLectureBankContent.addTextChangedListener {
            checkUploadAvailability()
        }
        binding.buttonLectureBankSearchLecture.setOnClickListener {
            lectureBankEditorSelectLectureActivityContract.launch(lectureBankEditorViewModel.lecture.value)
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
            layoutManager =
                LinearLayoutManager(this@LectureBankEditorActivity, RecyclerView.HORIZONTAL, false)
            adapter = lectureBankEditorUploadFileAdapter
        }

        binding.buttonLectureBankNewImage.setOnClickListener {
            lectureBankEditorImagePickerContract.launch(null)
        }

        binding.buttonLectureBankNewFile.setOnClickListener {
            lectureBankEditorFilePickerContract.launch(null)
        }

        binding.buttonLectureBankNewFinish.setOnClickListener {
            lectureBankEditorViewModel.uploadLectureBank(
                title = binding.editTextLectureBankTitle.text.toString(),
                content = binding.editTextLectureBankContent.text.toString(),
                semesterDate = binding.spinnerLectureBankSemester.selectedItem.toString(),
                category = selectedCategory.text.toString()
            )
        }

        binding.recyclerViewListFiles.isVisible = false
        binding.textViewPlzUploadFile.isVisible = true
        binding.textViewFileCountCapacity.isVisible = false
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
                    binding.spinnerLectureBankSemester.items.isNotEmpty() &&
                    !lectureBankEditorViewModel.uploadFiles.value.isNullOrEmpty()
    }

    private fun uploadSelectedUris(uris: List<Uri>) {
        val uploadedFileSize =
            lectureBankEditorViewModel.uploadFiles.value?.keys?.sumOf { it.size } ?: 0L
        val urisSize = uris.sumOf { it.getSize(applicationContext) ?: 0L }
        if (uploadedFileSize + urisSize > FILE_50MB)
            showFileSizeExceedDialog()
        else {
            lectureBankEditorViewModel.uploadFiles(applicationContext, uris)
        }
    }

    private fun showFileSizeExceedDialog() {
        showSimpleDialog(
            title = getString(R.string.lecture_bank_title_file_size_exceed),
            message = getString(R.string.lecture_bank_message_file_size_exceed),
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dialog, which ->
                dialog.dismiss()
            }
        )
    }

    companion object {
        private const val FILE_50MB = 50 * 1024 * 1024
    }


}