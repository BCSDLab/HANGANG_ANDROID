package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentTimetableCustomLectureBinding
import android.os.Bundle
import android.view.View

class TimetableCustomLectureFragment : ViewBindingFragment<FragmentTimetableCustomLectureBinding>(){
    override val layoutId = R.layout.fragment_timetable_custom_lecture

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.buttonTimetableCustomLectureAddTime.setOnClickListener {
            DialogUtil.makeWeekTimePickerDialog(requireContext()) { i: Int, startTime: Pair<Int, Int>, endTime: Pair<Int, Int> ->

            }.show()
        }
    }
}