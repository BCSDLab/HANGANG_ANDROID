package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.view.button.checkbox.FilledCheckBox
import `in`.hangang.core.view.childViews
import `in`.hangang.core.view.visibleGone
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.LectureFilter
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.databinding.FragmentTimetableLectureListBinding
import `in`.hangang.hangang.ui.timetable.adapter.TimetableLectureAdapter
import `in`.hangang.hangang.ui.timetable.contract.TimeTableLectureFilterActivityContract
import `in`.hangang.hangang.ui.timetable.listener.TimetableLectureListener
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableFragmentViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureListViewModel
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimetableLectureListFragment : ViewBindingFragment<FragmentTimetableLectureListBinding>() {
    override val layoutId = R.layout.fragment_timetable_lecture_list

    private val initLectureFilter = LectureFilter(
        classifications = listOf(),
        department = null,
        criteria = LectureFilter.CRITERIA_NAME_PROFESSOR,
        keyword = null
    )

    private val timetableFragmentViewModel: TimetableFragmentViewModel by sharedViewModel()
    private val timetableLectureListViewModel: TimetableLectureListViewModel by sharedViewModel()

    private val timetableLectureAdapter: TimetableLectureAdapter by lazy {
        TimetableLectureAdapter(
            requireContext()
        )
    }

    private val timetableLectureFilterActivityResult =
        registerForActivityResult(TimeTableLectureFilterActivityContract()) {
            if (it.apply) {
                timetableLectureListViewModel.setLectureFilter(it.lectureFilter)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        binding.radioGroupDepartment.childViews().forEach {
            (it as FilledCheckBox).setOnClickListener { _ ->
                if (!it.isChecked) {
                    timetableLectureListViewModel.setShowingDip(false)
                    timetableLectureListViewModel.setLectureFilter(
                        getLectureFilter().copy(department = null)
                    )
                } else {
                    binding.radioGroupDepartment.childViews().forEach { child ->
                        (child as FilledCheckBox).isChecked = false
                    }
                    it.isChecked = true
                    timetableLectureListViewModel.setShowingDip(it.id == R.id.radio_button_major_dip)
                    timetableLectureListViewModel.setLectureFilter(
                        getLectureFilter().copy(
                            department = when (it.id) {
                                R.id.radio_button_major_0 -> DEPARTMENT_LIBERAL
                                R.id.radio_button_major_1 -> DEPARTMENT_HRD
                                R.id.radio_button_major_2 -> DEPARTMENT_MECHANICAL
                                R.id.radio_button_major_3 -> DEPARTMENT_DESIGN
                                R.id.radio_button_major_4 -> DEPARTMENT_MECHATRONICS
                                R.id.radio_button_major_5 -> DEPARTMENT_INDUSTRIAL
                                R.id.radio_button_major_6 -> DEPARTMENT_ENERGY
                                R.id.radio_button_major_7 -> DEPARTMENT_CONVERGENCE
                                R.id.radio_button_major_8 -> DEPARTMENT_ELECTRONIC
                                R.id.radio_button_major_9 -> DEPARTMENT_COMPUTER
                                else -> null
                            }
                        )
                    )
                }
            }
        }
        binding.timetableLectureSearchBar.setSearchListener {
            timetableLectureListViewModel.setLectureFilter(
                getLectureFilter().copy(
                    keyword = it
                )
            )
        }
        binding.buttonTimetableLectureFilter.setOnClickListener {
            timetableLectureFilterActivityResult.launch(getLectureFilter())
        }


        binding.recyclerViewTimetableLectures.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTimetableLectures.adapter = timetableLectureAdapter
        binding.recyclerViewTimetableLectures.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    recyclerViewMoreScroll()
                }
            }
        })
        timetableLectureAdapter.timetableLectureListener = object : TimetableLectureListener {
            override fun onCheckedChange(position: Int, lectureTimeTable: LectureTimeTable) {
                if (position == -1) {
                    timetableFragmentViewModel.setDummyLectureTimeTable(null)
                } else
                    timetableFragmentViewModel.setDummyLectureTimeTable(lectureTimeTable)
            }

            override fun onAddButtonClicked(
                position: Int,
                lectureTimeTable: LectureTimeTable
            ): Boolean {
                with(timetableFragmentViewModel.checkLectureDuplication(lectureTimeTable)) {
                    if (this == null)
                        timetableFragmentViewModel.currentShowingTimeTable.value?.let {
                            timetableFragmentViewModel.addTimeTableLecture(
                                timetable = it,
                                lectureTimeTable = lectureTimeTable
                            )
                        }
                    else {
                        showTimetableDuplicatedDialog(this)
                    }
                }
                return false
            }

            override fun onRemoveButtonClicked(
                position: Int,
                lectureTimeTable: LectureTimeTable
            ): Boolean {
                timetableFragmentViewModel.currentShowingTimeTable.value?.let {
                    timetableFragmentViewModel.removeTimeTableLecture(
                        timetable = it,
                        lectureTimeTable = lectureTimeTable
                    )
                }
                return false
            }

            override fun onReviewButtonClicked(position: Int, lectureTimeTable: LectureTimeTable) {
                //TODO goto review activity
            }

            override fun onDipButtonClicked(position: Int, lectureTimeTable: LectureTimeTable) {
                timetableLectureListViewModel.toggleDipLecture(lectureTimeTable)
            }

        }
    }

    private fun initViewModel() {
        with(timetableFragmentViewModel) {
            lectureTimeTables.observe(viewLifecycleOwner) {
                timetableLectureAdapter.updateSelectedLectures(it)
            }
        }
        with(timetableLectureListViewModel) {
            isGetLecturesLoading.observe(viewLifecycleOwner) {
                binding.recyclerViewTimetableLecturesProgress.visibility = visibleGone(it)
            }
            lectures.observe(viewLifecycleOwner) {
                timetableLectureAdapter.updateItem(it)
            }
            timetableLectureChanged.observe(viewLifecycleOwner, EventObserver {
                timetableFragmentViewModel.currentShowingTimeTable.value?.let { timeTable ->
                    timetableFragmentViewModel.getAddedLectureTimeTables(timeTable)
                }
            })
            dips.observe(viewLifecycleOwner) {
                timetableLectureAdapter.updateDips(it)
            }
            lectureFilter.observe(viewLifecycleOwner) {
                updateLectureList()
            }
            resetLectureFilter.observe(viewLifecycleOwner) {
                binding.timetableLectureSearchBar.searchField.setText("")
                binding.radioGroupDepartment.childViews().forEach {
                    (it as FilledCheckBox).isChecked = false
                }
            }
        }
    }

    private fun updateLectureList() {
        if (timetableLectureListViewModel.isShowingDip.value == true) {
            timetableLectureListViewModel.getDipLectures(true)
        } else {
            timetableLectureListViewModel.getLectures(
                semesterDateId = timetableFragmentViewModel.currentShowingTimeTable.value?.semesterDateId
                    ?: 5
            )
        }
    }

    private fun recyclerViewMoreScroll() {
        if (timetableLectureListViewModel.isShowingDip.value == false) {
            timetableLectureListViewModel.getLectures()
        }
    }

    private fun showTimetableDuplicatedDialog(lectureTimeTable: LectureTimeTable) {
        DialogUtil.makeSimpleDialog(
            requireContext(),
            title = getString(R.string.timetable_duplicated_title),
            message = getString(R.string.timetable_duplicated_message, lectureTimeTable.name),
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            },
            cancelable = true
        ).show()
    }

    private fun getLectureFilter(): LectureFilter =
        timetableLectureListViewModel.lectureFilter.value ?: initLectureFilter
}