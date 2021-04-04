package `in`.hangang.hangang.ui.timetable.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.LectureFilter
import `in`.hangang.hangang.databinding.ActivityTimetableLectureFilterBinding
import `in`.hangang.hangang.ui.timetable.contract.TimeTableLectureFilterActivityContract.Companion.TIMETABLE_LECTURE_FILTER
import android.content.Intent
import android.os.Bundle

class TimetableLectureFilterActivity :
        ViewBindingActivity<ActivityTimetableLectureFilterBinding>() {
    override val layoutId: Int = R.layout.activity_timetable_lecture_filter
    val lectureFilter: LectureFilter by lazy {
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
        with(lectureFilter) {
            if (criteria == null) {
                binding.checkBoxFilterByName.isChecked = true
                binding.checkBoxFilterByProfessor.isChecked = true
            } else {
                binding.checkBoxFilterByName.isChecked = criteria == TIMETABLE_CRITERIA_LECTURE_NAME
                binding.checkBoxFilterByProfessor.isChecked = criteria == TIMETABLE_CRITERIA_PROFESSOR
            }

            binding.checkBoxFilterByClassificationLiberalRequired.isChecked = classifications.contains(CLASSIFICATION_LIBERAL_REQUIRED)
            binding.checkBoxFilterByClassificationLiberalChoice.isChecked = classifications.contains(CLASSIFICATION_LIBERAL_CHOICE)
            binding.checkBoxFilterByClassificationMajorRequired.isChecked = classifications.contains(CLASSIFICATION_MAJOR_REQUIRED)
            binding.checkBoxFilterByClassificationMajorChoice.isChecked = classifications.contains(CLASSIFICATION_MAJOR_CHOICE)
            binding.checkBoxFilterByClassificationMscRequired.isChecked = classifications.contains(CLASSIFICATION_MSC_REQUIRED)
            binding.checkBoxFilterByClassificationMscChoice.isChecked = classifications.contains(CLASSIFICATION_MSC_CHOICE)
            binding.checkBoxFilterByClassificationHrdChoice.isChecked = classifications.contains(CLASSIFICATION_HRD_REQUIRED)
            binding.checkBoxFilterByClassificationHrdChoice.isChecked = classifications.contains(CLASSIFICATION_HRD_CHOICE)
        }
    }

    private fun applyFilterAndFinish() {
        val classifications = mutableListOf<String>()
        if (binding.checkBoxFilterByClassificationLiberalRequired.isChecked)
            classifications.add(CLASSIFICATION_LIBERAL_REQUIRED)

        if (binding.checkBoxFilterByClassificationLiberalChoice.isChecked)
            classifications.add(CLASSIFICATION_LIBERAL_CHOICE)

        if (binding.checkBoxFilterByClassificationMajorRequired.isChecked)
            classifications.add(CLASSIFICATION_MAJOR_REQUIRED)

        if (binding.checkBoxFilterByClassificationMajorChoice.isChecked)
            classifications.add(CLASSIFICATION_MAJOR_CHOICE)

        if (binding.checkBoxFilterByClassificationMscRequired.isChecked)
            classifications.add(CLASSIFICATION_MSC_REQUIRED)

        if (binding.checkBoxFilterByClassificationMscChoice.isChecked)
            classifications.add(CLASSIFICATION_MSC_CHOICE)

        if (binding.checkBoxFilterByClassificationHrdRequired.isChecked)
            classifications.add(CLASSIFICATION_HRD_REQUIRED)

        if (binding.checkBoxFilterByClassificationHrdChoice.isChecked)
            classifications.add(CLASSIFICATION_HRD_CHOICE)

        val criteria = when {
            binding.checkBoxFilterByName.isChecked -> TIMETABLE_CRITERIA_LECTURE_NAME
            binding.checkBoxFilterByProfessor.isChecked -> TIMETABLE_CRITERIA_PROFESSOR
            else -> null
        }

        setResult(RESULT_OK, Intent().apply {
            putExtra(TIMETABLE_LECTURE_FILTER, lectureFilter.copy(
                    criteria = criteria,
                    classifications = classifications
            ))
        })
        finish()
    }

    private fun resetFilter() {
        binding.checkBoxFilterByClassificationLiberalRequired.isChecked = false
        binding.checkBoxFilterByClassificationLiberalChoice.isChecked = false
        binding.checkBoxFilterByClassificationMajorRequired.isChecked = false
        binding.checkBoxFilterByClassificationMajorChoice.isChecked = false
        binding.checkBoxFilterByClassificationMscRequired.isChecked = false
        binding.checkBoxFilterByClassificationMscChoice.isChecked = false
        binding.checkBoxFilterByClassificationHrdChoice.isChecked = false
        binding.checkBoxFilterByClassificationHrdChoice.isChecked = false

        binding.checkBoxFilterByName.isChecked = true
        binding.checkBoxFilterByProfessor.isChecked = true
    }
}