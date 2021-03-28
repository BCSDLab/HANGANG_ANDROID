package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.CustomTimetableTimestamp
import `in`.hangang.hangang.databinding.FragmentTimetableCustomLectureBinding
import `in`.hangang.hangang.ui.timetable.adapter.TimetableCustomLectureTimeAdapter
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableCustomLectureViewModel
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimetableCustomLectureFragment : ViewBindingFragment<FragmentTimetableCustomLectureBinding>(){
    override val layoutId = R.layout.fragment_timetable_custom_lecture

    private val timetableCustomLectureViewModel: TimetableCustomLectureViewModel by sharedViewModel()

    private val timetableCustomLectureTimeAdapter : TimetableCustomLectureTimeAdapter by lazy {
        TimetableCustomLectureTimeAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initValue()
    }

    private fun initValue() {
        binding.editTextCustomLectureName.setText("")
        binding.editTextCustomLectureProfessor.setText("")
        timetableCustomLectureViewModel.init()
    }

    private fun initViewModel() {
        with(timetableCustomLectureViewModel) {
            timestamp.observe(viewLifecycleOwner) {
                timetableCustomLectureTimeAdapter.updateList(it)
            }
            customTimetableAdded.observe(viewLifecycleOwner, EventObserver {

            })
        }
    }

    private fun initView() {
        binding.buttonTimetableCustomLectureAddTime.setOnClickListener {
            DialogUtil.makeWeekTimePickerDialog(requireContext()) { i: Int, startTime: Pair<Int, Int>, endTime: Pair<Int, Int> ->

            }.show()
        }
        binding.recyclerViewCustomLectureTimestamp.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCustomLectureTimestamp.adapter = timetableCustomLectureTimeAdapter
    }
}