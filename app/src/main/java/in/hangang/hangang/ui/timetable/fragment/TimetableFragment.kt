package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.view.appbar.appBarImageButton
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.core.view.appbar.interfaces.OnAppBarButtonClickListener
import `in`.hangang.core.view.edittext.SingleLineEditText
import `in`.hangang.core.view.goneVisible
import `in`.hangang.core.view.visibleGone
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.databinding.FragmentTimetableBinding
import `in`.hangang.hangang.ui.timetable.adapter.TimetableLectureAdapter
import `in`.hangang.hangang.ui.timetable.contract.TimetableListActivityContract
import `in`.hangang.hangang.ui.timetable.listener.TimetableLectureListener
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableFragmentViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.TimetableUtil
import `in`.hangang.hangang.util.file.FileUtil
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimetableFragment : ViewBindingFragment<FragmentTimetableBinding>() {

    override val layoutId = R.layout.fragment_timetable

    private val lectureTimetableDummyViews = arrayListOf<View>()

    private val timetableViewModel: TimetableViewModel by viewModel()
    private val timetableFragmentViewModel: TimetableFragmentViewModel by viewModel()
    private val timetableLectureViewModel: TimetableLectureViewModel by viewModel()

    private val fileUtil: FileUtil by inject()
    private val timetableUtil: TimetableUtil by inject()

    private val behavior by lazy { BottomSheetBehavior.from(binding.timetableLectureListContainer) }
    private val timetableLectureAdapter: TimetableLectureAdapter by lazy { TimetableLectureAdapter(requireContext()) }

    private val timetableListActivityResult = registerForActivityResult(TimetableListActivityContract()) {
        it.selectedTimetable?.let { timetable ->
            timetableFragmentViewModel.setCurrentShowingTimeTable(timetable)
        }
        if (it.timetableListChanged) timetableViewModel.getTimetables()
    }

    private val appBarOpenTimetableListButton by lazy {
        appBarImageButton(R.drawable.ic_list)
    }

    private val appBarMoreMenuButton by lazy {
        appBarImageButton(R.drawable.ic_more)
    }

    private val appBarCloseButton by lazy {
        appBarImageButton(R.drawable.ic_close)
    }

    private val appBarAddManuallyButton by lazy {
        appBarTextButton(
                getString(R.string.timetable_add_manually),
                width = ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAppBar()
        initBottomSheet()
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        with(timetableFragmentViewModel) {

            mode.observe(viewLifecycleOwner) {
                when (it) {
                    TimetableFragmentViewModel.Mode.MODE_NORMAL -> {
                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                        appBarOpenTimetableListButton.visibility = View.VISIBLE
                        appBarMoreMenuButton.visibility = View.VISIBLE
                        appBarAddManuallyButton.visibility = View.GONE
                        appBarCloseButton.visibility = View.GONE
                        hideLectureTimetableDummyViews()
                    }
                    TimetableFragmentViewModel.Mode.MODE_EDIT -> {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        appBarOpenTimetableListButton.visibility = View.GONE
                        appBarMoreMenuButton.visibility = View.GONE
                        appBarAddManuallyButton.visibility = View.VISIBLE
                        appBarCloseButton.visibility = View.VISIBLE
                        showLectureTimetableDummyViews()
                    }
                }
            }

            currentShowingTimeTable.observe(viewLifecycleOwner, this@TimetableFragment::updateTimeTable)

            captured.observe(viewLifecycleOwner, this@TimetableFragment::saveImageToFile)

            lectureTimetableViews.observe(viewLifecycleOwner, this@TimetableFragment::showLectureTimeTable)
            lectureTimeTables.observe(viewLifecycleOwner) {
                timetableLectureAdapter.updateSelectedLectures(it)
            }
        }
        with(timetableViewModel) {

            mainTimeTable.observe(viewLifecycleOwner) {
                timetableFragmentViewModel.setCurrentShowingTimeTable(it)
            }

            setMainTimeTable.observe(viewLifecycleOwner, EventObserver {
                timetableFragmentViewModel.setCurrentShowingTimeTable(it)
                showMainTimetableSetDialog()
            })

            timetableNameModified.observe(viewLifecycleOwner, EventObserver {
                timetableFragmentViewModel.setCurrentShowingTimeTable(
                        timetableFragmentViewModel.currentShowingTimeTable.value!!.copy(name = it)
                )
            })
        }
        with(timetableLectureViewModel) {

            isGetLecturesLoading.observe(viewLifecycleOwner) {
                binding.recyclerViewTimetableLecturesProgress.visibility = visibleGone(it)
            }
            lectures.observe(viewLifecycleOwner) {
                timetableLectureAdapter.updateItem(it)
            }
            timetableLectureAdded.observe(viewLifecycleOwner, EventObserver {
                timetableFragmentViewModel.currentShowingTimeTable.value?.let { timeTable ->
                    timetableFragmentViewModel.renderTimeTable(timetableUtil, timeTable)
                }
            })
            timetableLectureRemoved.observe(viewLifecycleOwner, EventObserver {
                timetableFragmentViewModel.currentShowingTimeTable.value?.let { timeTable ->
                    timetableFragmentViewModel.renderTimeTable(timetableUtil, timeTable)
                }
            })

        }

        timetableViewModel.getTimetables()
    }

    private fun initView() {
        binding.fabEdit.setOnClickListener {
            timetableFragmentViewModel.switchToEditMode()
        }
        binding.radioGroupDepartment.setOnCheckedChangeListener { group, checkedId ->
            //TODO department 정확한 학부 명칭 안 뒤 파라미터로 추가
            timetableLectureViewModel.getLectures(
                    semesterDateId = timetableFragmentViewModel.currentShowingTimeTable.value?.semesterDateId
                            ?: 5
            )
        }
    }

    private fun initBottomSheet() {
        behavior.isHideable = true
        behavior.halfExpandedRatio = 0.5f
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.skipCollapsed = true
        behavior.isFitToContents = true
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_HIDDEN -> {
                        timetableFragmentViewModel.switchToNormalMode()
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED, BottomSheetBehavior.STATE_EXPANDED -> {
                        timetableFragmentViewModel.switchToEditMode()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.timetableScrollView.setPadding(0, 0, 0,
                        (bottomSheet.height * (slideOffset + 1)).toInt()
                )
            }
        })

        binding.recyclerViewTimetableLectures.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTimetableLectures.adapter = timetableLectureAdapter
        binding.recyclerViewTimetableLectures.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    timetableLectureViewModel.getLectures()
                }
            }
        })
        timetableLectureAdapter.timetableLectureListener = object : TimetableLectureListener {
            override fun onCheckedChange(position: Int, lectureTimeTable: LectureTimeTable) {
                if (position == -1) {
                    hideLectureTimetableDummyViews()
                } else
                    timetableUtil.getTimetableDummyView(listOf(lectureTimeTable)).withThread().subscribe(
                            this@TimetableFragment::setLectureTimetableDummyViews
                    ) {}
            }

            override fun onAddButtonClicked(
                    position: Int,
                    lectureTimeTable: LectureTimeTable
            ): Boolean {
                with(timetableFragmentViewModel.checkLectureDuplication(lectureTimeTable)) {
                    if (this == null)
                        timetableLectureViewModel.addTimeTableLecture(
                                timetableId = timetableFragmentViewModel.currentShowingTimeTable.value?.id
                                        ?: 0,
                                lectureId = lectureTimeTable.id
                        )
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
                timetableLectureViewModel.removeTimeTableLecture(
                        timetableId = timetableFragmentViewModel.currentShowingTimeTable.value?.id
                                ?: 0,
                        lectureId = lectureTimeTable.id
                )
                return false
            }

            override fun onReviewButtonClicked(position: Int, lectureTimeTable: LectureTimeTable) {
                TODO("Not yet implemented")
            }

            override fun onDipButtonClicked(position: Int, lectureTimeTable: LectureTimeTable) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun initAppBar() {
        binding.appBar.apply {
            addViewInLeft(appBarOpenTimetableListButton)
            addViewInLeft(appBarCloseButton)
            addViewInRight(appBarMoreMenuButton)
            addViewInRight(appBarAddManuallyButton)

            onAppBarButtonButtonClickListener = object : OnAppBarButtonClickListener {
                override fun onClickViewInLeftContainer(view: View, index: Int) {
                    when (view) {
                        appBarOpenTimetableListButton -> {
                            openTimetableList()
                        }
                        appBarCloseButton -> {
                            timetableFragmentViewModel.switchToNormalMode()
                        }
                    }
                }

                override fun onClickViewInRightContainer(view: View, index: Int) {
                    when (view) {
                        appBarAddManuallyButton -> {

                        }
                        appBarMoreMenuButton -> {
                            showTimetablePopupMenu(appBarMoreMenuButton)
                        }
                    }
                }
            }
        }
    }

    private fun showTimetablePopupMenu(v: View) {
        val popupMenu = PopupMenu(requireContext(), v)
        popupMenu.menuInflater.inflate(R.menu.menu_timetable, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_set_main_timetable -> {
                    timetableFragmentViewModel.currentShowingTimeTable.value?.let { it1 -> timetableViewModel.setMainTimeTable(it1) }
                }
                R.id.menu_item_save_image -> {
                    timetableFragmentViewModel.saveToBitmap(binding.timetableContainer)
                }
                R.id.menu_item_edit_timetable_name -> {
                    showEditTimeTableNameDialog()
                }
                R.id.menu_item_remove_timetable -> {
                    showRemoveTimeTableDialog()
                }
            }
            true
        }
    }


    //리스트 형태의 시간표 관리 화면 표시
    private fun openTimetableList() {
        timetableListActivityResult.launch(
                timetableFragmentViewModel.currentShowingTimeTable.value
        )
    }

    private fun updateTimeTable(timetable: TimeTable) {
        binding.appBar.title = timetable.name.toString()
        timetableFragmentViewModel.renderTimeTable(timetableUtil, timetable)
    }

    private fun showLectureTimeTable(lectureTimeTableViews: List<View>) {
        binding.timetableLayout.removeAllViews()
        lectureTimeTableViews.forEach {
            binding.timetableLayout.addView(it)
        }
    }

    private fun setLectureTimetableDummyViews(lectureTimeTableViews: List<View>) {
        lectureTimetableDummyViews.forEach {
            binding.timetableLayout.removeView(it)
        }
        lectureTimetableDummyViews.clear()
        lectureTimetableDummyViews.addAll(lectureTimeTableViews)
        lectureTimetableDummyViews.forEach {
            binding.timetableLayout.addView(it)
        }
    }

    private fun hideLectureTimetableDummyViews() {
        lectureTimetableDummyViews.forEach {
            it.visibility = goneVisible(true)
        }
    }

    private fun showLectureTimetableDummyViews() {
        lectureTimetableDummyViews.forEach {
            it.visibility = goneVisible(false)
        }
    }

    private fun saveImageToFile(bitmap: Bitmap) {
        requireWriteStorage {
            fileUtil.saveImageToPictures(
                    bitmap = bitmap,
                    fileName = "${timetableFragmentViewModel.currentShowingTimeTable.value?.name ?: "Unknown"}.jpg"
            )
                    .withThread()
                    .handleProgress(timetableFragmentViewModel)
                    .subscribe({
                        LogUtil.d(it.path)
                    }, {
                        it.printStackTrace()
                    })
        }
    }

    private fun showMainTimetableSetDialog() {
        DialogUtil.makeSimpleDialog(requireContext(),
                title = getString(R.string.timetable_dialog_finish_main_timetable_title),
                message = getString(R.string.timetable_dialog_finish_main_timetable_message),
                positiveButtonText = getString(R.string.ok),
                positiveButtonOnClickListener = { dialog, _ ->
                    dialog.dismiss()
                },
                cancelable = true
        ).show()
    }

    private fun showEditTimeTableNameDialog() {
        if (timetableFragmentViewModel.currentShowingTimeTable.value != null) {
            val editText = SingleLineEditText(requireContext()).apply {
                editText.hint = timetableFragmentViewModel.currentShowingTimeTable.value!!.name
            }

            DialogUtil.makeViewDialog(requireContext(),
                    title = getString(R.string.timetable_dialog_edit_timetable_name_title),
                    view = editText,
                    cancelable = true,
                    positiveButtonText = getString(R.string.ok),
                    negativeButtonText = getString(R.string.cancel),
                    positiveButtonOnClickListener = { dialog, _ ->
                        timetableViewModel.modifyTimeTableName(timetableFragmentViewModel.currentShowingTimeTable.value!!, editText.text.toString())
                        dialog.dismiss()
                    },
                    negativeButtonOnClickListener = { dialog, _ ->
                        dialog.dismiss()
                    }).show()
        }
    }

    private fun showRemoveTimeTableDialog() {
        if (timetableFragmentViewModel.currentShowingTimeTable.value != null) {
            DialogUtil.makeSimpleDialog(requireContext(),
                    title = getString(R.string.timetable_dialog_remove_timetable_title),
                    message = "",
                    positiveButtonText = getString(R.string.ok),
                    negativeButtonText = getString(R.string.close),
                    negativeButtonOnClickListener = { dialog, _ ->
                        dialog.dismiss()
                    },
                    positiveButtonOnClickListener = { dialog, _ ->
                        timetableViewModel.removeTimetable(timetableFragmentViewModel.currentShowingTimeTable.value!!)
                        dialog.dismiss()
                    },
                    cancelable = true
            ).show()
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
}