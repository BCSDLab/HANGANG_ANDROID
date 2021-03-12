package `in`.hangang.hangang.ui.timetable

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.appbar.appBarImageButton
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityTimetableListBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TimetableListActivity : ViewBindingActivity<ActivityTimetableListBinding>() {
    override val layoutId = R.layout.activity_timetable_list
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppBar()
    }

    private fun initAppBar() {
        binding.appBar.apply {
            addViewInLeft(appBarImageButton(R.drawable.ic_close).apply {
                setOnClickListener { onBackPressed() }
            })
            addViewInRight(appBarTextButton(context.getString(R.string.timetable_list_add_timetable)).apply {
                setOnClickListener { showAddTimetableActivity() }
            })
        }
    }

    private fun showAddTimetableActivity() {
        TODO("Not yet implemented")
    }
}