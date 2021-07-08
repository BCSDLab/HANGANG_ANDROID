package `in`.hangang.hangang.ui.timetable.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.semester.Semester
import `in`.hangang.hangang.databinding.ActivityTimetableAddBinding
import `in`.hangang.hangang.ui.timetable.contract.TimeTableAddActivityContract
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableAddActivityViewModel
import `in`.hangang.hangang.util.DateUtil
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

        timetableAddActivityViewModel.getSemesterNow()
    }

    private fun initViewModel() {
        with(timetableAddActivityViewModel) {
            addingAvailable.observe(this@TimetableAddActivity) {
                binding.buttonAddTimetable.isEnabled = it
            }
            isAdded.observe(this@TimetableAddActivity, EventObserver {
                    setResult(TimeTableAddActivityContract.TIMETABLE_RESULT_IS_ADDED, Intent().apply {
                        putExtra(TimeTableAddActivityContract.TIMETABLE_IS_ADDED, true)
                    })
                    finish()
            })
            semesterNow.observe(this@TimetableAddActivity) {
                if(!it.hasBeenHandled)
                    initSemesterDateId(it.peekContent())

                binding.textViewTimetableSemester.text = getString(R.string.timetable_add_timetable_semester, DateUtil.getYear(it.peekContent().startTime).toString())
            }
            error.observe(this@TimetableAddActivity, EventObserver {
                when (it.code) {
                    API_ERROR_CODE_TIMETABLE_EXCEED -> showTimetableSemesterLimitErrorDialog()
                    else -> showCommonErrorDialog(it.message ?: "")
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
                    semesterDateId = getSemesterDateId(),
                    name = binding.editTextTimetableName.text.toString()
            )
        }
        binding.editTextTimetableName.addTextChangedListener {
            timetableAddActivityViewModel.checkAddingAvailability(it.toString())
        }

    }

    private fun initSemesterDateId(semester: Semester) {
        binding.radioButtonTimetableSemester1.isChecked = false
        binding.radioButtonTimetableSemester2.isChecked = false
        binding.radioButtonTimetableSemester3.isChecked = false
        binding.radioButtonTimetableSemester4.isChecked = false
        when (semester.id) {
            TIMETABLE_SEMESTER_1 -> binding.radioButtonTimetableSemester1.isChecked = true
            TIMETABLE_SEMESTER_SUMMER -> binding.radioButtonTimetableSemester2.isChecked = true
            TIMETABLE_SEMESTER_2 -> binding.radioButtonTimetableSemester3.isChecked = true
            TIMETABLE_SEMESTER_WINTER -> binding.radioButtonTimetableSemester4.isChecked = true
        }
    }

    private fun getSemesterDateId() = when (binding.radioGroupSemester.checkedRadioButtonId) {
        R.id.radio_button_timetable_semester_1 -> TIMETABLE_SEMESTER_1
        R.id.radio_button_timetable_semester_2 -> TIMETABLE_SEMESTER_SUMMER
        R.id.radio_button_timetable_semester_3 -> TIMETABLE_SEMESTER_2
        R.id.radio_button_timetable_semester_4 -> TIMETABLE_SEMESTER_WINTER
        else -> 0
    }

    private fun showCommonErrorDialog(message: String) {
        DialogUtil.makeSimpleDialog(
            this,
            message = message,
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            },
            cancelable = true
        ).show()
    }
}