package `in`.hangang.hangang.ui.timetable

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.appbar.appBarImageButton
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentTimetableBinding
import android.os.Bundle
import android.view.View
import android.view.ViewGroup

class TimetableFragment : ViewBindingFragment<FragmentTimetableBinding>() {

    override val layoutId = R.layout.fragment_timetable

    private val appBarOpenTimetableListButton by lazy {
        appBarImageButton(R.drawable.ic_list).apply {
            setOnClickListener {

            }
        }
    }

    private val appBarMoreMenuButton by lazy {
        appBarImageButton(R.drawable.ic_more).apply {
            setOnClickListener {

            }
        }
    }

    private val appBarCloseButton by lazy {
        appBarImageButton(R.drawable.ic_close).apply {
            setOnClickListener {

            }
        }
    }

    private val appBarAddManuallyButton by lazy {
        appBarTextButton(
                getString(R.string.timetable_add_manually),
                width = ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setOnClickListener {

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAppBar()
    }

    private fun initAppBar() {
        binding.appBar.apply {
            addViewInLeft(appBarOpenTimetableListButton)
            addViewInLeft(appBarCloseButton)
            addViewInRight(appBarMoreMenuButton)
            addViewInRight(appBarAddManuallyButton)
        }
    }

    //리스트 형태의 시간표 관리 화면 표시
    private fun openTimetableList() {

    }

    //강의 추가 바텀 시트 표시
    private fun openNewTimetableItemDialog() {

    }
}