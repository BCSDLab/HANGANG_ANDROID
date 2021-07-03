package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.TIMETABLE_INVALID_TIMETABLE_ID
import `in`.hangang.hangang.data.entity.timetable.CustomTimetableTimestamp
import `in`.hangang.hangang.data.entity.timetable.LectureTimeTable
import `in`.hangang.hangang.databinding.FragmentTimetableCustomLectureBinding
import `in`.hangang.hangang.ui.customview.timetable.TimetableUtil
import `in`.hangang.hangang.ui.timetable.adapter.TimetableCustomLectureTimeAdapter
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import `in`.hangang.hangang.util.HangangDialogUtil
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimetableCustomLectureFragment :
        ViewBindingFragment<FragmentTimetableCustomLectureBinding>() {
    override val layoutId = R.layout.fragment_timetable_custom_lecture

    private val timetableViewModel: TimetableViewModel by sharedViewModel()

    private val timetableCustomLectureTimeAdapter: TimetableCustomLectureTimeAdapter by lazy {
        TimetableCustomLectureTimeAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initValue()

        timetableViewModel.initCustomLectureValue()
    }

    private fun initValue() {
        binding.editTextCustomLectureName.setText("")
        binding.editTextCustomLectureProfessor.setText("")
    }

    private fun initViewModel() {
        with(timetableViewModel) {
            timestamp.observe(viewLifecycleOwner) {
                timetableCustomLectureTimeAdapter.updateList(it)
                checkValidation(
                        binding.editTextCustomLectureName.text.toString(),
                        binding.editTextCustomLectureProfessor.text.toString()
                )
                timetableViewModel.setDummyLectureTimeTable(
                        LectureTimeTable(
                                classTime = TimetableUtil.convertCustomTimetableTimestampToApiExpression(it)
                        )
                )
            }
            customLectureAdded.observe(viewLifecycleOwner, EventObserver {
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
                timetableViewModel.addTimestamp(
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
                    timetableViewModel.timestamp.value!![position]
            ) { week: Int, startTime: Pair<Int, Int>, endTime: Pair<Int, Int> ->
                timetableViewModel.modifyTimestamp(
                        position,
                        CustomTimetableTimestamp(week, startTime, endTime)
                )
            }.show()
        }, { view, position ->
            timetableViewModel.removeTimestamp(position)
        })

        binding.editTextCustomLectureName.addTextChangedListener {
            timetableViewModel.checkValidation(
                    binding.editTextCustomLectureName.text.toString(),
                    binding.editTextCustomLectureProfessor.text.toString()
            )
        }
        binding.editTextCustomLectureProfessor.addTextChangedListener {
            timetableViewModel.checkValidation(
                    binding.editTextCustomLectureName.text.toString(),
                    binding.editTextCustomLectureProfessor.text.toString()
            )
        }
        binding.buttonTimetableCustomLectureAdd.setOnClickListener {
            timetableViewModel.addCustomLecture(
                    name = binding.editTextCustomLectureName.text.toString(),
                    professor = binding.editTextCustomLectureProfessor.text.toString(),
                    classTime = TimetableUtil.convertCustomTimetableTimestampToApiExpression(timetableViewModel.timestamp.value!!),
                    timetableId = timetableViewModel.displayingTimeTable.value?.id
                            ?: TIMETABLE_INVALID_TIMETABLE_ID
            )
        }
    }

    override fun onDestroyView() {
        with(timetableViewModel) {
            timestamp.removeObservers(viewLifecycleOwner)
            customLectureAdded.removeObservers(viewLifecycleOwner)
            availableAddingCustomTimetable.removeObservers(viewLifecycleOwner)
        }
        super.onDestroyView()
    }
}