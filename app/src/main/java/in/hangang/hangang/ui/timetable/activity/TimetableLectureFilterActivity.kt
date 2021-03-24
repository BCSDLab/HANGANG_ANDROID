package `in`.hangang.hangang.ui.timetable.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.LectureFilter
import `in`.hangang.hangang.databinding.ActivityTimetableLectureFilterBinding
import android.content.Intent
import android.os.Bundle

class TimetableLectureFilterActivity :
    ViewBindingActivity<ActivityTimetableLectureFilterBinding>() {
    override val layoutId: Int = R.layout.activity_timetable_lecture_filter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable_lecture_filter)

        binding.buttonReset.setOnClickListener {
            resetFilter()
        }

        binding.buttonApply.setOnClickListener {
            applyFilterAndFinish()
        }
    }

    private fun applyFilterAndFinish() {
        val classifications = mutableListOf<String>()
        if (binding.checkBoxFilterByClassificationLiberalRequired.isChecked)
            classifications.add("교양필수")

        if (binding.checkBoxFilterByClassificationLiberalChoice.isChecked)
            classifications.add("교양선택")

        if (binding.checkBoxFilterByClassificationMajorRequired.isChecked)
            classifications.add("전공필수")

        if (binding.checkBoxFilterByClassificationMajorChoice.isChecked)
            classifications.add("전공선택")

        if (binding.checkBoxFilterByClassificationMscRequired.isChecked)
            classifications.add("MSC필수")

        if (binding.checkBoxFilterByClassificationMscChoice.isChecked)
            classifications.add("MSC선택")

        if (binding.checkBoxFilterByClassificationHrdChoice.isChecked)
            classifications.add("HRD필수")

        if (binding.checkBoxFilterByClassificationHrdChoice.isChecked)
            classifications.add("HRD선택")

        val criteria =
            (if (binding.checkBoxFilterByName.isChecked) 1 else 0) shl 2 +
                    (if (binding.checkBoxFilterByProfessor.isChecked) 1 else 0)

        Intent().apply {
            putExtra("LectureFilter", LectureFilter(criteria, classifications))
        }
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

        binding.checkBoxFilterByName.isChecked = false
        binding.checkBoxFilterByProfessor.isChecked = false
    }
}