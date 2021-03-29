package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.view.appbar.appBarImageButton
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.core.view.appbar.interfaces.OnAppBarButtonClickListener
import `in`.hangang.core.view.edittext.SingleLineEditText
import `in`.hangang.core.view.goneVisible
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.databinding.FragmentTimetableBinding
import `in`.hangang.hangang.ui.timetable.contract.TimetableListActivityContract
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableFragmentViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureDetailViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureListViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.TimetableUtil
import `in`.hangang.hangang.util.file.FileUtil
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimetableFragment : ViewBindingFragment<FragmentTimetableBinding>() {

    override val layoutId = R.layout.fragment_timetable

    private val lectureTimetableDummyViews = arrayListOf<View>()

    private val timetableViewModel: TimetableViewModel by sharedViewModel()
    private val timetableFragmentViewModel: TimetableFragmentViewModel by sharedViewModel()
    private val timetableLectureListViewModel: TimetableLectureListViewModel by sharedViewModel()
    private val timetableLectureDetailViewModel: TimetableLectureDetailViewModel by sharedViewModel()

    private val timetableLectureListFragment : TimetableLectureListFragment by lazy {
        TimetableLectureListFragment()
    }
    private val timetableCustomLectureFragment : TimetableCustomLectureFragment by lazy {
        TimetableCustomLectureFragment()
    }
    private val timetableLectureDetailFragment : TimetableLectureDetailFragment by lazy {
        TimetableLectureDetailFragment()
    }

    private val fileUtil: FileUtil by inject()
    private val timetableUtil: TimetableUtil by inject()

    private val behavior by lazy { BottomSheetBehavior.from(binding.timetableBottomSheetContainer) }

    private val timetableListActivityResult = registerForActivityResult(TimetableListActivityContract()) {
        if(it.resultCode == RESULT_OK) {
            it.selectedTimetable?.let { timetable ->
                timetableFragmentViewModel.setCurrentShowingTimeTable(timetable)
            }
            if (it.timetableListChanged) timetableViewModel.getTimetables()
            timetableFragmentViewModel.setMode(TimetableFragmentViewModel.Mode.MODE_NORMAL)
        }
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
                        changeBottomSheetFragment(timetableLectureListFragment)
                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                        appBarOpenTimetableListButton.visibility = View.VISIBLE
                        appBarMoreMenuButton.visibility = View.VISIBLE
                        appBarAddManuallyButton.visibility = View.GONE
                        appBarCloseButton.visibility = View.GONE
                        hideLectureTimetableDummyViews()
                    }
                    TimetableFragmentViewModel.Mode.MODE_LECTURE_LIST -> {
                        changeBottomSheetFragment(timetableLectureListFragment)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        appBarOpenTimetableListButton.visibility = View.GONE
                        appBarMoreMenuButton.visibility = View.GONE
                        appBarAddManuallyButton.visibility = View.VISIBLE
                        appBarCloseButton.visibility = View.VISIBLE
                        showLectureTimetableDummyViews()
                    }
                    TimetableFragmentViewModel.Mode.MODE_CUSTOM_LECTURE -> {
                        changeBottomSheetFragment(timetableCustomLectureFragment, true)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        appBarOpenTimetableListButton.visibility = View.GONE
                        appBarMoreMenuButton.visibility = View.GONE
                        appBarAddManuallyButton.visibility = View.VISIBLE
                        appBarCloseButton.visibility = View.VISIBLE
                        showLectureTimetableDummyViews()
                    }
                    TimetableFragmentViewModel.Mode.MODE_LECTURE_DETAIL -> {
                        changeBottomSheetFragment(timetableLectureDetailFragment, true)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        appBarOpenTimetableListButton.visibility = View.VISIBLE
                        appBarMoreMenuButton.visibility = View.VISIBLE
                        appBarAddManuallyButton.visibility = View.GONE
                        appBarCloseButton.visibility = View.GONE
                        hideLectureTimetableDummyViews()
                    }
                }
            }

            currentShowingTimeTable.observe(viewLifecycleOwner, this@TimetableFragment::updateTimeTable)

            captured.observe(viewLifecycleOwner, this@TimetableFragment::saveImageToFile)

            lectureTimeTables.observe(viewLifecycleOwner) {
                timetableUtil.getTimetableView(it)
                        .withThread()
                        .subscribe(this@TimetableFragment::showLectureTimeTable, {})
                if(timetableFragmentViewModel.mode.value == TimetableFragmentViewModel.Mode.MODE_LECTURE_DETAIL)
                    timetableFragmentViewModel.setMode(TimetableFragmentViewModel.Mode.MODE_NORMAL)
            }

            selectedTimeTable.observe(viewLifecycleOwner) {

            }

            dummyTimeTable.observe(viewLifecycleOwner) {
                    timetableUtil.getTimetableDummyView(if(it == null) listOf() else listOf(it))
                        .withThread()
                        .subscribe(this@TimetableFragment::showLectureDummyTimetable)
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

        timetableViewModel.getTimetables()
    }

    private fun initView() {
        binding.fabEdit.setOnClickListener {
            timetableFragmentViewModel.setMode(TimetableFragmentViewModel.Mode.MODE_LECTURE_LIST)
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
                        timetableFragmentViewModel.setMode(TimetableFragmentViewModel.Mode.MODE_NORMAL)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.timetableScrollView.setPadding(0, 0, 0,
                        (bottomSheet.height * (slideOffset + 1)).toInt()
                )
            }
        })

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
                            timetableFragmentViewModel.setMode(TimetableFragmentViewModel.Mode.MODE_NORMAL)
                        }
                    }
                }

                override fun onClickViewInRightContainer(view: View, index: Int) {
                    when (view) {
                        appBarAddManuallyButton -> {
                            timetableFragmentViewModel.setMode(TimetableFragmentViewModel.Mode.MODE_CUSTOM_LECTURE)
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
        timetableLectureListViewModel.getDipLectures(false)
        timetableFragmentViewModel.getAddedLectureTimeTables(timetable)
        timetableLectureListViewModel.resetLectureFilter()
    }

    private fun showLectureTimeTable(lectureTimeTableViews: Map<View, LectureTimeTable>) {
        binding.timetableLayout.removeAllViews()
        lectureTimeTableViews.keys.forEach { view ->
            view.setOnClickListener {
                timetableFragmentViewModel.setMode(TimetableFragmentViewModel.Mode.MODE_LECTURE_DETAIL)
                lectureTimeTableViews[view]?.let { lectureTimeTable -> timetableLectureDetailViewModel.initWithLectureTimetable(lectureTimeTable) }
            }
            binding.timetableLayout.addView(view)
        }
    }

    private fun showLectureDummyTimetable(lectureTimeTableViews: List<View>) {
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

    private fun changeBottomSheetFragment(fragment: Fragment, addtoBackStack: Boolean = false) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.timetable_bottom_sheet_container, fragment)
            if(addtoBackStack) addToBackStack(null)
            commit()
        }
    }

}