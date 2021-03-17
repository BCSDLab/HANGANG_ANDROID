package `in`.hangang.hangang.ui.timetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.appbar.appBarImageButton
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.core.view.appbar.interfaces.OnAppBarButtonClickListener
import `in`.hangang.core.view.visibleGone
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentTimetableBinding
import `in`.hangang.hangang.ui.timetable.adapter.TimetableLectureAdapter
import `in`.hangang.hangang.ui.timetable.contract.TimetableListActivityContract
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableFragmentViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.file.FileUtil
import `in`.hangang.hangang.util.handleProgress
import `in`.hangang.hangang.util.withThread
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimetableFragment : ViewBindingFragment<FragmentTimetableBinding>() {

    override val layoutId = R.layout.fragment_timetable

    private val timetableViewModel: TimetableViewModel by viewModel()
    private val timetableFragmentViewModel: TimetableFragmentViewModel by viewModel()
    private val timetableLectureViewModel: TimetableLectureViewModel by viewModel()

    private val fileUtil: FileUtil by inject()

    private val behavior by lazy { BottomSheetBehavior.from(binding.timetableLectureListContainer) }
    private val timetableLectureAdapter = TimetableLectureAdapter()

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
                    }
                    TimetableFragmentViewModel.Mode.MODE_EDIT -> {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        appBarOpenTimetableListButton.visibility = View.GONE
                        appBarMoreMenuButton.visibility = View.GONE
                        appBarAddManuallyButton.visibility = View.VISIBLE
                        appBarCloseButton.visibility = View.VISIBLE
                    }
                }
            }
            currentShowingTimeTable.observe(viewLifecycleOwner) {
                binding.appBar.title = it.name.toString()
                //TODO Render timetable
            }
            captured.observe(viewLifecycleOwner) { bitmap ->
                requireWriteStorage {
                    fileUtil.saveImageToPictures(
                            bitmap = bitmap,
                            fileName = "${currentShowingTimeTable.value?.name ?: "Unknown"}.jpg"
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
        }
        with(timetableViewModel) {
            mainTimeTable.observe(viewLifecycleOwner) {
                timetableFragmentViewModel.setCurrentShowingTimeTable(it)
            }
        }
        with(timetableLectureViewModel) {
            isGetLecturesLoading.observe(viewLifecycleOwner) {
                binding.recyclerViewTimetableLecturesProgress.visibility = visibleGone(it)
            }
            getLecturesErrorMessage.observe(viewLifecycleOwner) {
            }
            lectures.observe(viewLifecycleOwner) {
                timetableLectureAdapter.updateItem(it)
            }
        }

        timetableViewModel.getTimetables()
    }

    private fun initView() {
        binding.fabEdit.setOnClickListener {
            timetableFragmentViewModel.switchToEditMode()
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
                binding.timetableContainer.setPadding(0, 0, 0,
                        (bottomSheet.height * (slideOffset + 1)).toInt()
                )
            }
        })

        binding.recyclerViewTimetableLectures.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTimetableLectures.adapter = timetableLectureAdapter
        timetableLectureViewModel.getLectures()
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
                    timetableFragmentViewModel.saveToBitmap(binding.timetableLayout)
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
}