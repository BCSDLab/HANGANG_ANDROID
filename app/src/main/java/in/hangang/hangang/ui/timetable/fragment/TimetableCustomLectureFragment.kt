package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.CustomTimetableTimestamp
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.databinding.FragmentTimetableCustomLectureBinding
import `in`.hangang.hangang.ui.timetable.adapter.TimetableCustomLectureTimeAdapter
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableCustomLectureViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableFragmentViewModel
import `in`.hangang.hangang.util.HangangDialogUtil
import `in`.hangang.hangang.util.TimetableUtil
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimetableCustomLectureFragment :
    ViewBindingFragment<FragmentTimetableCustomLectureBinding>() {
    override val layoutId = R.layout.fragment_timetable_custom_lecture

    private val timetableCustomLectureViewModel: TimetableCustomLectureViewModel by sharedViewModel()
    private val timetableFragmentViewModel : TimetableFragmentViewModel by sharedViewModel()

    private val timetableCustomLectureTimeAdapter: TimetableCustomLectureTimeAdapter by lazy {
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
                checkValidation(
                    binding.editTextCustomLectureName.text.toString(),
                    binding.editTextCustomLectureProfessor.text.toString()
                )
                timetableFragmentViewModel.setDummyLectureTimeTable(
                    LectureTimeTable(
                        classTime = TimetableUtil.toExp(it)
                    )
                )
            }
            customTimetableAdded.observe(viewLifecycleOwner, EventObserver {
                timetableFragmentViewModel.getAddedLectureTimeTables(timetableFragmentViewModel.currentShowingTimeTable.value!!)
                initValue()
            })
            availableAddingCustomTimetable.observe(viewLifecycleOwner) {
                binding.buttonTimetableCustomLectureAdd.isEnabled = it
            }
        }
    }

    private fun initView() {
        binding.buttonTimetableCustomLectureAddTime.setOnClickListener {
            HangangDialogUtil.makeTimetableCustomLectureTimePickerDialog(requireContext()) { week: Int, startTime: Pair<Int, Int>, endTime: Pair<Int, Int> ->
                timetableCustomLectureViewModel.addTimestamp(
                    CustomTimetableTimestamp(week, startTime, endTime)
                )
            }.show()
        }
        binding.recyclerViewCustomLectureTimestamp.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerViewCustomLectureTimestamp.adapter = timetableCustomLectureTimeAdapter
        timetableCustomLectureTimeAdapter.setOnItemClickListener({ view, position ->
            HangangDialogUtil.makeTimetableCustomLectureTimePickerDialog(
                requireContext(),
                timetableCustomLectureViewModel.timestamp.value!![position]
            ) { week: Int, startTime: Pair<Int, Int>, endTime: Pair<Int, Int> ->
                timetableCustomLectureViewModel.modifyTimestamp(
                    position,
                    CustomTimetableTimestamp(week, startTime, endTime)
                )
            }.show()
        }, { view, position ->
            timetableCustomLectureViewModel.removeTimestamp(position)
        })

        binding.editTextCustomLectureName.addTextChangedListener {
            timetableCustomLectureViewModel.checkValidation(
                binding.editTextCustomLectureName.text.toString(),
                binding.editTextCustomLectureProfessor.text.toString()
            )
        }
        binding.editTextCustomLectureProfessor.addTextChangedListener {
            timetableCustomLectureViewModel.checkValidation(
                binding.editTextCustomLectureName.text.toString(),
                binding.editTextCustomLectureProfessor.text.toString()
            )
        }
        binding.buttonTimetableCustomLectureAdd.setOnClickListener {
            with(TimetableUtil.toExp(timetableCustomLectureViewModel.timestamp.value!!)) {
                if(timetableFragmentViewModel.checkLectureDuplication(this) == null) {
                    timetableCustomLectureViewModel.addCustomLecture(
                            name = binding.editTextCustomLectureName.text.toString(),
                            professor = binding.editTextCustomLectureProfessor.text.toString(),
                            classTime = TimetableUtil.toExp(timetableCustomLectureViewModel.timestamp.value!!),
                            userTimetableId = timetableFragmentViewModel.currentShowingTimeTable.value?.id ?: 5
                    )
                }
            }
        }
    }
}