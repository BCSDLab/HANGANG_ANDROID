package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.progressdialog.changeProgressState
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.view.appbar.appBarImageButton
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.core.view.appbar.interfaces.OnAppBarButtonClickListener
import `in`.hangang.core.view.edittext.SingleLineEditText
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.databinding.FragmentTimetableBinding
import `in`.hangang.hangang.ui.timetable.contract.TimetableListActivityContract
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureDetailViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureListViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.file.FileUtil
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimetableFragment : ViewBindingFragment<FragmentTimetableBinding>() {

    override val layoutId = R.layout.fragment_timetable

    private var selectedLecturePositionTop = 0

    private val timetableViewModel: TimetableViewModel by sharedViewModel()
    private val timetableLectureListViewModel: TimetableLectureListViewModel by sharedViewModel()
    private val timetableLectureDetailViewModel: TimetableLectureDetailViewModel by sharedViewModel()

    private val timetableLectureListFragment: TimetableLectureListFragment = TimetableLectureListFragment()
    private val timetableCustomLectureFragment: TimetableCustomLectureFragment = TimetableCustomLectureFragment()
    private val timetableLectureDetailFragment: TimetableLectureDetailFragment = TimetableLectureDetailFragment()

    private val fileUtil: FileUtil by inject()

    private val behavior by lazy { BottomSheetBehavior.from(binding.timetableBottomSheetContainer) }

    private val timetableListActivityResult = registerForActivityResult(TimetableListActivityContract()) {
        if (it.resultCode == RESULT_OK) {
            it.selectedTimetable?.let { timetable ->
                timetableViewModel.setDisplayingTimeTable(timetable)
            }
            if (it.timetableListChanged) timetableViewModel.getTimetables()
            timetableViewModel.setMode(TimetableViewModel.Mode.MODE_NORMAL)
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
                width = ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timetableViewModel.getMainTimeTable()

        initView()
        initAppBar()
        initBottomSheet()
        initViewModel()
    }

    private fun initViewModel() {
        with(timetableViewModel) {
            isLoading.observe(viewLifecycleOwner) {
                changeProgressState(it)
            }
            mode.observe(viewLifecycleOwner) {
                when (it) {
                    TimetableViewModel.Mode.MODE_NORMAL -> {
                        changeBottomSheetFragment(timetableLectureListFragment)
                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                        appBarOpenTimetableListButton.visibility = View.VISIBLE
                        appBarMoreMenuButton.visibility = View.VISIBLE
                        appBarAddManuallyButton.visibility = View.GONE
                        appBarCloseButton.visibility = View.GONE
                        binding.timetableLayout.isShowingDummyView = false
                    }
                    TimetableViewModel.Mode.MODE_LECTURE_LIST -> {
                        changeBottomSheetFragment(timetableLectureListFragment)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        appBarOpenTimetableListButton.visibility = View.GONE
                        appBarMoreMenuButton.visibility = View.GONE
                        appBarAddManuallyButton.visibility = View.VISIBLE
                        appBarCloseButton.visibility = View.VISIBLE
                        binding.timetableLayout.isShowingDummyView = true
                    }
                    TimetableViewModel.Mode.MODE_CUSTOM_LECTURE -> {
                        changeBottomSheetFragment(timetableCustomLectureFragment)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        appBarOpenTimetableListButton.visibility = View.GONE
                        appBarMoreMenuButton.visibility = View.GONE
                        appBarAddManuallyButton.visibility = View.VISIBLE
                        appBarCloseButton.visibility = View.VISIBLE
                        binding.timetableLayout.isShowingDummyView = true
                    }
                    TimetableViewModel.Mode.MODE_LECTURE_DETAIL -> {
                        changeBottomSheetFragment(timetableLectureDetailFragment)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        appBarOpenTimetableListButton.visibility = View.VISIBLE
                        appBarMoreMenuButton.visibility = View.VISIBLE
                        appBarAddManuallyButton.visibility = View.GONE
                        appBarCloseButton.visibility = View.GONE
                        binding.timetableLayout.isShowingDummyView = true
                    }
                }
            }

            displayingTimeTable.observe(viewLifecycleOwner, this@TimetableFragment::updateTimeTable)

            timetableBitmapImage.observe(viewLifecycleOwner, this@TimetableFragment::saveImageToFile)

            lectureTimetablesInTimetable.observe(viewLifecycleOwner) {
                binding.timetableLayout.removeAllTimetableItems()
                binding.timetableLayout.addTimetableItem(*it.toTypedArray())
            }

            selectedTimetable.observe(viewLifecycleOwner) { lectureTimeTable ->
                if (lectureTimeTable != null) {
                    timetableLectureDetailViewModel.initWithLectureTimetable(lectureTimeTable)
                }
                timetableViewModel.setMode(TimetableViewModel.Mode.MODE_LECTURE_DETAIL)
            }

            dummyTimeTable.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.timetableLayout.removeAllTimetableDummyItems()
                    binding.timetableLayout.addTimetableDummyItem(it)
                }
            }

            duplicatedLectureTimetable.observe(viewLifecycleOwner, EventObserver {
                it?.let { lecture -> showTimetableDuplicatedDialog(lecture) }
            })
            mainTimetableEvent.observe(viewLifecycleOwner, EventObserver {
                timetableViewModel.setDisplayingTimeTable(it)
            })

            setMainTimetableEvent.observe(viewLifecycleOwner, EventObserver {
                showMainTimetableSetDialog()
            })
            timetableNameModifiedEvent.observe(viewLifecycleOwner, EventObserver {
                binding.appBar.title = it
            })
        }

    }

    private fun initView() {
        binding.fabEdit.setOnClickListener {
            timetableViewModel.setMode(TimetableViewModel.Mode.MODE_LECTURE_LIST)
        }
        with(binding.timetableLayout) {
            setOnClickListener {
                timetableViewModel.setMode(TimetableViewModel.Mode.MODE_NORMAL)
            }
            setTimetableItemClickListener { view: View, lectureTimeTable: LectureTimeTable ->
                timetableViewModel.setMode(TimetableViewModel.Mode.MODE_LECTURE_DETAIL)
                binding.timetableScrollView.smoothScrollTo(0, view.top)
                selectedLecturePositionTop = view.top
                timetableLectureDetailViewModel.initWithLectureTimetable(lectureTimeTable)
            }
            setScrollViewCallback({
            }, {
                it[0].viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.timetableScrollView.smoothScrollTo(0, it[0].top)
                        it[0].viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            })
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
                        timetableViewModel.setMode(TimetableViewModel.Mode.MODE_NORMAL)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        if (timetableViewModel.mode.value == TimetableViewModel.Mode.MODE_LECTURE_DETAIL) {
                            binding.timetableScrollView.smoothScrollTo(0, selectedLecturePositionTop)
                            selectedLecturePositionTop = 0
                        }
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.timetableScrollView.setPadding(
                        0, 0, 0,
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
                            timetableViewModel.setMode(TimetableViewModel.Mode.MODE_NORMAL)
                        }
                    }
                }

                override fun onClickViewInRightContainer(view: View, index: Int) {
                    when (view) {
                        appBarAddManuallyButton -> {
                            timetableViewModel.setMode(TimetableViewModel.Mode.MODE_CUSTOM_LECTURE)
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
                    timetableViewModel.displayingTimeTable.value?.let { it1 -> timetableViewModel.setMainTimetable(it1.id) }
                }
                R.id.menu_item_save_image -> {
                    timetableViewModel.getTimetableBitmapImage(binding.timetableContainer)
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
                timetableViewModel.displayingTimeTable.value
        )
    }

    private fun updateTimeTable(timetable: TimeTable) {
        binding.appBar.title = timetable.name.toString()
        timetableLectureListViewModel.getScrapedLectures(false)
        timetableViewModel.getLectureTimeTablesInTimeTable(timetable)
        timetableLectureListViewModel.resetLectureFilter()
    }

    private fun saveImageToFile(bitmap: Bitmap) {
        requireWriteStorage {
            fileUtil.saveImageToPictures(
                    bitmap = bitmap,
                    fileName = "${timetableViewModel.displayingTimeTable.value?.name ?: "Unknown"}.jpg"
            )
                    .withThread()
                    .handleProgress(timetableViewModel)
                    .subscribe({
                        LogUtil.d(it.path)
                    }, {
                        it.printStackTrace()
                    })
        }
    }

    private fun showMainTimetableSetDialog() {
        DialogUtil.makeSimpleDialog(
                requireContext(),
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
        if (timetableViewModel.displayingTimeTable.value != null) {
            val editText = SingleLineEditText(requireContext()).apply {
                editText.hint = timetableViewModel.displayingTimeTable.value!!.name
            }

            DialogUtil.makeViewDialog(requireContext(),
                    title = getString(R.string.timetable_dialog_edit_timetable_name_title),
                    view = editText,
                    cancelable = true,
                    positiveButtonText = getString(R.string.ok),
                    negativeButtonText = getString(R.string.cancel),
                    positiveButtonOnClickListener = { dialog, _ ->
                        timetableViewModel.modifyTimeTableName(
                                timetableViewModel.displayingTimeTable.value!!,
                                editText.text.toString()
                        )
                        dialog.dismiss()
                    },
                    negativeButtonOnClickListener = { dialog, _ ->
                        dialog.dismiss()
                    }).show()
        }
    }

    private fun showRemoveTimeTableDialog() {
        if (timetableViewModel.displayingTimeTable.value != null) {
            DialogUtil.makeSimpleDialog(
                    requireContext(),
                    title = getString(R.string.timetable_dialog_remove_timetable_title),
                    message = "",
                    positiveButtonText = getString(R.string.ok),
                    negativeButtonText = getString(R.string.close),
                    negativeButtonOnClickListener = { dialog, _ ->
                        dialog.dismiss()
                    },
                    positiveButtonOnClickListener = { dialog, _ ->
                        timetableViewModel.removeTimetable(timetableViewModel.displayingTimeTable.value!!)
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

    private fun changeBottomSheetFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.timetable_bottom_sheet_container, fragment)
            commit()
        }
    }

    override fun onDestroyView() {
        with(timetableViewModel) {
            isLoading.removeObservers(viewLifecycleOwner)
            mode.removeObservers(viewLifecycleOwner)
            displayingTimeTable.removeObservers(viewLifecycleOwner)
            timetableBitmapImage.removeObservers(viewLifecycleOwner)
            lectureTimetablesInTimetable.removeObservers(viewLifecycleOwner)
            selectedTimetable.removeObservers(viewLifecycleOwner)
            dummyTimeTable.removeObservers(viewLifecycleOwner)
            duplicatedLectureTimetable.removeObservers(viewLifecycleOwner)
            mainTimetableEvent.removeObservers(viewLifecycleOwner)
            setMainTimetableEvent.removeObservers(viewLifecycleOwner)
            timetableNameModifiedEvent.removeObservers(viewLifecycleOwner)
        }
        super.onDestroyView()
    }
}