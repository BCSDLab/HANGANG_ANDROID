package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.view.setVisibility
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentTimetableLectureDetailBinding
import `in`.hangang.hangang.ui.customview.timetable.TimetableUtil
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureDetailViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimetableLectureDetailFragment : ViewBindingFragment<FragmentTimetableLectureDetailBinding>() {
    override val layoutId = R.layout.fragment_timetable_lecture_detail

    private val timetableViewModel: TimetableViewModel by sharedViewModel()
    private val timetableLectureDetailViewModel: TimetableLectureDetailViewModel by sharedViewModel()
    private val navController: NavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

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
                            lectureTimeTable = lectureTimeTable,
                        closeBottomSheet = true
                    )
                }
            }
        }
        binding.buttonTimetableLectureDetailSaveMemo.setOnClickListener {
            timetableLectureDetailViewModel.updateMemo(binding.editTextMemo.text.toString())
        }
        binding.imageButtonGotoReview.setOnClickListener {
            var lectureId = timetableLectureDetailViewModel.lectureTimetable.value?.lectureId
            timetableLectureDetailViewModel.getLectureId(lectureId!!)

        }
        binding.container.setOnClickListener {
            //Blocking being closed this fragment
        }
    }

    private fun initViewModel() {
        with(timetableLectureDetailViewModel) {
            isLoading.observe(viewLifecycleOwner) {
                binding.container.setVisibility(!it)
                binding.progressTimetableLectureDetail.setVisibility(it)
            }
            lectureTimetable.observe(viewLifecycleOwner) {
                binding.lecture = it
                binding.executePendingBindings()
                binding.textViewTimetableLectureDetailTime.text =
                        TimetableUtil.convertApiExpressionToKoreatechClassTime(
                                requireContext(), it.classTime
                                ?: "[]"
                        )
            }
            memo.observe(viewLifecycleOwner, EventObserver {
                binding.editTextMemo.setText(it)
            })
            lecture.observe(viewLifecycleOwner){
                val bundle = Bundle()
                bundle.putParcelable("lecture", it)
                navController.navigate(R.id.navigation_evaluation, bundle)
            }
        }
    }


    override fun onDestroyView() {
        with(timetableLectureDetailViewModel) {
            isLoading.removeObservers(viewLifecycleOwner)
            lectureTimetable.removeObservers(viewLifecycleOwner)
            memo.removeObservers(this@TimetableLectureDetailFragment)
        }
        super.onDestroyView()
    }
}