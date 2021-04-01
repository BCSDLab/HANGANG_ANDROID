package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentTimetableLectureDetailBinding
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureDetailViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import `in`.hangang.hangang.util.TimetableUtil
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimetableLectureDetailFragment : ViewBindingFragment<FragmentTimetableLectureDetailBinding>() {
    override val layoutId = R.layout.fragment_timetable_lecture_detail

    private val timetableViewModel: TimetableViewModel by sharedViewModel()
    private val timetableLectureDetailViewModel: TimetableLectureDetailViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() {
        binding.imageButtonRemoveLecture.setOnClickListener {
            timetableViewModel.displayingTimeTable.value?.let { timetable ->
                timetableLectureDetailViewModel.lectureTimetable.value?.let { lectureTimeTable ->
                    timetableViewModel.removeTimeTableLecture(
                        timetable = timetable,
                        lectureTimeTable = lectureTimeTable
                    )
                }
            }
        }
        binding.buttonTimetableLectureDetailSaveMemo.setOnClickListener {
            timetableLectureDetailViewModel.updateMemo(binding.editTextMemo.text.toString())
        }
        binding.imageButtonGotoReview.setOnClickListener {
            //TODO 강의평 이동
        }
    }

    private fun initViewModel() {
        with(timetableLectureDetailViewModel) {
            lectureTimetable.observe(viewLifecycleOwner) {
                binding.lecture = it
                binding.executePendingBindings()
                binding.textViewTimetableLectureDetailTime.text = TimetableUtil.toString(requireContext(), it.classTime ?: "[]")
            }
            memo.observe(viewLifecycleOwner, EventObserver{
                binding.editTextMemo.setText(it)
            })
        }
    }
}