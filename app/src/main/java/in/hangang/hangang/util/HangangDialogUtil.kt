package `in`.hangang.hangang.util

import `in`.hangang.core.R
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.hangang.constant.*
import `in`.hangang.hangang.data.entity.CustomTimetableTimestamp
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.NumberPicker
import androidx.core.view.children

object HangangDialogUtil {

    fun makeTimetableCustomLectureTimePickerDialog(
        context: Context,
        customTimetableTimestamp: CustomTimetableTimestamp? = null,
        listener: (week: Int, startTime: Pair<Int, Int>, endTime: Pair<Int, Int>) -> Unit
    ): Dialog {
        val pickerView = LayoutInflater.from(context).inflate(R.layout.layout_time_picker, null)

        val weekPicker = pickerView.findViewById<NumberPicker>(R.id.number_picker_week)
        val startHourPicker = pickerView.findViewById<NumberPicker>(R.id.number_picker_start_hour)
        val startMinutePicker =
            pickerView.findViewById<NumberPicker>(R.id.number_picker_start_minute)
        val endHourPicker = pickerView.findViewById<NumberPicker>(R.id.number_picker_end_hour)
        val endMinutePicker = pickerView.findViewById<NumberPicker>(R.id.number_picker_end_minute)

        weekPicker.apply {
            children.iterator().forEach {
                if (it is EditText) it.filters = arrayOfNulls(0)    // remove default input filter
            }
            setFormatter {
                context.resources.getStringArray(R.array.timetable_picker_weeks)[it]
            }
            minValue = TIMETABLE_MON
            maxValue = TIMETABLE_FRI
            value = customTimetableTimestamp?.week ?: 0
        }

        startHourPicker.apply {
            minValue = TIMETABLE_DEFAULT_START_HOUR
            maxValue = TIMETABLE_DEFAULT_END_HOUR
            value = customTimetableTimestamp?.startTime?.first ?: TIMETABLE_DEFAULT_START_HOUR
        }

        endHourPicker.apply {
            minValue = TIMETABLE_DEFAULT_START_HOUR
            maxValue = TIMETABLE_DEFAULT_END_HOUR
            value = customTimetableTimestamp?.endTime?.first ?: TIMETABLE_DEFAULT_START_HOUR + 1
        }

        startMinutePicker.apply {
            children.iterator().forEach {
                if (it is EditText) it.filters = arrayOfNulls(0)    // remove default input filter
            }
            setFormatter {
                context.resources.getStringArray(R.array.timetable_picker_minutes)[it]
            }
            minValue = TIMETABLE_MINUTE_0
            maxValue = TIMETABLE_MINUTE_30
            value = customTimetableTimestamp?.startTime?.second ?: TIMETABLE_MINUTE_0
        }

        endMinutePicker.apply {
            children.iterator().forEach {
                if (it is EditText) it.filters = arrayOfNulls(0)    // remove default input filter
            }
            setFormatter {
                context.resources.getStringArray(R.array.timetable_picker_minutes)[it]
            }
            minValue = TIMETABLE_MINUTE_0
            maxValue = TIMETABLE_MINUTE_30
            value = customTimetableTimestamp?.endTime?.second ?: TIMETABLE_MINUTE_0
        }

        return DialogUtil.makeViewDialog(
            context,
            view = pickerView,
            positiveButtonText = context.getString(R.string.ok),
            negativeButtonText = context.getString(R.string.close),
            positiveButtonOnClickListener = { dialog, _ ->
                listener(
                    weekPicker.value,
                    Pair(startHourPicker.value, startMinutePicker.value * 30),
                    Pair(endHourPicker.value, endMinutePicker.value * 30)
                )
                dialog.dismiss()
            },
            negativeButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            }
        )
    }
}