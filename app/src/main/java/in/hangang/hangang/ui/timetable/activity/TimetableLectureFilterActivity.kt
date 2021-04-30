package `in`.hangang.hangang.ui.timetable.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.LectureFilter
import `in`.hangang.hangang.databinding.ActivityTimetableLectureFilterBinding
import `in`.hangang.hangang.ui.timetable.contract.TimeTableLectureFilterActivityContract.Companion.TIMETABLE_LECTURE_FILTER
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox

class TimetableLectureFilterActivity :
        ViewBindingActivity<ActivityTimetableLectureFilterBinding>() {
    override val layoutId: Int = R.layout.activity_timetable_lecture_filter
    private val mapCheckBox: Map<String, CheckBox> by lazy {
        mapOf(
                CLASSIFICATION_LIBERAL_REQUIRED to binding.checkBoxFilterByClassificationLiberalRequired,
                CLASSIFICATION_LIBERAL_CHOICE to binding.checkBoxFilterByClassificationLiberalChoice,
                CLASSIFICATION_MAJOR_REQUIRED to binding.checkBoxFilterByClassificationMajorRequired,
                CLASSIFICATION_MAJOR_CHOICE to binding.checkBoxFilterByClassificationMajorChoice,
                CLASSIFICATION_MSC_REQUIRED to binding.checkBoxFilterByClassificationMscRequired,
                CLASSIFICATION_MSC_CHOICE to binding.checkBoxFilterByClassificationMscChoice,
                CLASSIFICATION_HRD_REQUIRED to binding.checkBoxFilterByClassificationHrdChoice,
                CLASSIFICATION_HRD_CHOICE to binding.checkBoxFilterByClassificationHrdChoice
        )
    }
    private val lectureFilter: LectureFilter by lazy {
        intent.extras?.getParcelable(TIMETABLE_LECTURE_FILTER) ?: LectureFilter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        binding.buttonReset.setOnClickListener {
            resetFilter()
        }

        binding.buttonApply.setOnClickListener {
            applyFilterAndFinish()
        }

        binding.buttonClose.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun initView() {
        resetFilter()
        with(lectureFilter) {
            if (criteria == null) {
                binding.checkBoxFilterByName.isChecked = true
                binding.checkBoxFilterByProfessor.isChecked = true
            } else {
                binding.checkBoxFilterByName.isChecked = criteria == TIMETABLE_CRITERIA_LECTURE_NAME
                binding.checkBoxFilterByProfessor.isChecked = criteria == TIMETABLE_CRITERIA_PROFESSOR
            }

            classifications.forEach {
                mapCheckBox[it]?.isChecked = true
            }
        }
    }

    private fun applyFilterAndFinish() {
        val classifications = mutableListOf<String>()
        mapCheckBox.forEach {
            if (it.value.isChecked) classifications.add(it.key)
        }

        val criteria = when {
            binding.checkBoxFilterByName.isChecked -> TIMETABLE_CRITERIA_LECTURE_NAME
            binding.checkBoxFilterByProfessor.isChecked -> TIMETABLE_CRITERIA_PROFESSOR
            else -> null
        }

        setResult(RESULT_OK, Intent().apply {
            putExtra(
                    TIMETABLE_LECTURE_FILTER, lectureFilter.copy(
                    criteria = criteria,
                    classifications = classifications
            )
            )
        })
        finish()
    }

    private fun resetFilter() {
        mapCheckBox.forEach {
            it.value.isChecked = false
        }

        binding.checkBoxFilterByName.isChecked = true
        binding.checkBoxFilterByProfessor.isChecked = true
    }
}