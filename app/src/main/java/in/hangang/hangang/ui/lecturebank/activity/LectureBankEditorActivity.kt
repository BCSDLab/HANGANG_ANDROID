package `in`.hangang.hangang.ui.lecturebank.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.lecturebank.LectureBank
import `in`.hangang.hangang.databinding.ActivityLectureBankEditorBinding
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankEditorActivityContract
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankEditorSelectLectureActivityContract
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankEditorViewModel
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.TypedValue
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureBankEditorActivity : ViewBindingActivity<ActivityLectureBankEditorBinding>() {
    override val layoutId = R.layout.activity_lecture_bank_editor

    private val lectureBankEditorViewModel: LectureBankEditorViewModel by viewModel()

    private val lectureBankEditorSelectActivityContract = registerForActivityResult(
        LectureBankEditorSelectLectureActivityContract()
    ) {
        if (it.resultCode == RESULT_OK)
            lectureBankEditorViewModel.setLecture(it.lecture)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()

        initWithLectureBank(intent.extras?.getParcelable(LectureBankEditorActivityContract.LECTURE_BANK))
    }

    private fun initWithLectureBank(lectureBank: LectureBank?) {
        with(binding) {
            buttonLectureBankSearchLecture.setOnClickListener {
                lectureBankEditorSelectActivityContract.launch(lectureBankEditorViewModel.lecture.value)
            }

            if (lectureBank != null) {
                editTextLectureBankTitle.setText(lectureBank.title)
                textViewLectureBankLecture.text = SpannableStringBuilder()
                    .inSpans(
                        ForegroundColorSpan(
                            TypedValue().apply {
                                this@LectureBankEditorActivity.theme.resolveAttribute(
                                    android.R.attr.textColorPrimary,
                                    this,
                                    true
                                )
                            }.data
                        ),
                        RelativeSizeSpan(14f / 12f)
                    ) {
                        append(lectureBank.lecture.name)
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
                        )
                    ) {
                        append(lectureBank.lecture.professor)
                    }

            }
        }

    }

    private fun initViewModel() {
        with(lectureBankEditorViewModel) {
            lecture.observe(this@LectureBankEditorActivity) {
                binding.textViewLectureBankLecture.text = it?.name ?: ""
            }
            uploadFiles.observe(this@LectureBankEditorActivity) {
                binding.recyclerViewListFiles.isVisible = !it.isNullOrEmpty()
                binding.textViewPlzUploadFile.isVisible = it.isNullOrEmpty()
                binding.textViewFileCountCapacity
            }
        }
    }

    private fun initView() {
        binding.spinnerLectureBankSemester.items
    }
}