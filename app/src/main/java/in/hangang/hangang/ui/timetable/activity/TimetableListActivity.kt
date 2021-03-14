package `in`.hangang.hangang.ui.timetable.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.appbar.appBarImageButton
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.core.view.appbar.interfaces.OnAppBarButtonClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.TimeTable
import `in`.hangang.hangang.databinding.ActivityTimetableListBinding
import `in`.hangang.hangang.ui.timetable.adapter.TimetableTimetablesAdapter
import `in`.hangang.hangang.ui.timetable.listener.TimetableListRecyclerViewOnItemClickListener
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableListActivityViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimetableListActivity : ViewBindingActivity<ActivityTimetableListBinding>() {
    override val layoutId = R.layout.activity_timetable_list

    private val timetableViewModel : TimetableViewModel by viewModel()
    private val timetableListActivityViewModel : TimetableListActivityViewModel by viewModel()

    private val timetableAdapter = TimetableTimetablesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppBar()
        initRecyclerView()
        initViewModel()
    }

    private fun initRecyclerView() {
        binding.recyclerViewTimetableList.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTimetableList.adapter = timetableAdapter
        timetableAdapter.selectedTimeTableId = intent.extras?.getInt("selectedTimeTableId") ?: 0
        timetableAdapter.timetableListRecyclerViewOnItemClickListener = object : TimetableListRecyclerViewOnItemClickListener {
            override fun onTimeTableItemClick(timetable: TimeTable) {
                setResult(1, Intent().putExtra("selectedTimeTableId", timetable.id))
                finish()
            }
        }
    }

    private fun initViewModel() {
        binding.activityViewModel = timetableListActivityViewModel

        with(timetableViewModel) {
            timetables.observe(this@TimetableListActivity) {
                timetableAdapter.updateItem(it)
                timetableListActivityViewModel.updateTimetableSize(it.size)
            }
            mainTimeTable.observe(this@TimetableListActivity) {
                timetableAdapter.mainTimeTableId = it.id
            }
        }
        with(timetableListActivityViewModel) {
            currentTimetableSize.observe(this@TimetableListActivity) {
                binding.textViewTimetableListLimit.text = "$it/50"
            }
        }

        timetableViewModel.getTimetables()
    }

    private fun initAppBar() {
        binding.appBar.apply {
            addViewInLeft(appBarImageButton(R.drawable.ic_close))
            addViewInRight(appBarTextButton(context.getString(R.string.timetable_list_add_timetable)))
            onAppBarButtonButtonClickListener = object : OnAppBarButtonClickListener {
                override fun onClickViewInLeftContainer(view: View, index: Int) {
                    onBackPressed()
                }

                override fun onClickViewInRightContainer(view: View, index: Int) {
                    showAddTimetableActivity()
                }
            }
        }
    }

    private fun showAddTimetableActivity() {
        TODO("Not yet implemented")
    }
}