package `in`.hangang.hangang.ui.timetable.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityTimetableAddBinding
import `in`.hangang.hangang.ui.timetable.contract.TimeTableAddActivityContract
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableAddActivityViewModel
import `in`.hangang.hangang.util.*
import android.content.Intent
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimetableAddActivity : ViewBindingActivity<ActivityTimetableAddBinding>() {
    override val layoutId = R.layout.activity_timetable_add

    private val timetableAddActivityViewModel: TimetableAddActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        with(timetableAddActivityViewModel) {
            addingAvailable.observe(this@TimetableAddActivity) {
                binding.buttonAddTimetable.isEnabled = it
            }
            added.observe(this@TimetableAddActivity, EventObserver {
                if (it) {
                    setResult(1, Intent().apply {
                        putExtra(TimeTableAddActivityContract.TIMETABLE_ADDED, true)
                    })
                    finish()
                }
            })
            error.observe(this@TimetableAddActivity, EventObserver {
                when (it.code) {
                    24 -> showTimetableSemesterLimitErrorDialog()
                }
            })
        }
    }

    private fun showTimetableSemesterLimitErrorDialog() {
        DialogUtil.makeSimpleDialog(
                context = this,
                title = getString(R.string.timetable_exceed_title),
                message = getString(R.string.timetable_exceed_message_semester),
                positiveButtonText = getString(R.string.ok),
                positiveButtonOnClickListener = { dialog, _ -> dialog.dismiss() }
        ).show()
    }

    private fun initView() {
        binding.buttonAddTimetable.setOnClickListener {
            timetableAddActivityViewModel.addTimeTable(
                    year = 2021,
                    semester = getSemester(),
                    name = binding.editTextTimetableName.text.toString())
        }
        binding.editTextTimetableName.addTextChangedListener {
            timetableAddActivityViewModel.checkAddingAvailability(it.toString())
        }
    }

    private fun getSemester() = when (binding.radioGroupSemester.checkedRadioButtonId) {
        R.id.radio_button_timetable_semester_1 -> SEMESTER_1
        R.id.radio_button_timetable_semester_2 -> SEMESTER_SUMMER
        R.id.radio_button_timetable_semester_3 -> SEMESTER_2
        R.id.radio_button_timetable_semester_4 -> SEMESTER_WINTER
        else -> 0
    }
}