package `in`.hangang.hangang.ui.view

import `in`.hangang.hangang.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.HorizontalScrollView

class HorizontalMajorsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attributeSet, defStyleAttr) {

    private val majorsLayout = LayoutInflater.from(context).inflate(R.layout.layout_horizontal_majors, this as ViewGroup)

    private val majorCheckBoxes = mapOf(
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_0) to context.getString(R.string.major_full_0),
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_1) to context.getString(R.string.major_full_1),
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_2) to context.getString(R.string.major_full_2),
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_3) to context.getString(R.string.major_full_3),
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_4) to context.getString(R.string.major_full_4),
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_5) to context.getString(R.string.major_full_5),
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_6) to context.getString(R.string.major_full_6),
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_7) to context.getString(R.string.major_full_7),
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_8) to context.getString(R.string.major_full_8),
        majorsLayout.findViewById<CheckBox>(R.id.radio_button_major_9) to context.getString(R.string.major_full_9)
    ).also { map ->
        map.keys.forEachIndexed { position, checkBox ->
            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    map.keys.forEach { key -> key.isChecked = false }
                    checkBox.isChecked = true
                }
                if(checkBox.isChecked) {
                    onSelectionChangedListener?.onSelectionChanged(position, map[it])
                } else {
                    onSelectionChangedListener?.onSelectionChanged(-1, null)
                }
            }
        }
    }

    var onSelectionChangedListener : OnSelectionChangedListener? = null

    inline fun setOnSelectionChangedListener(crossinline onSelectionChanged : (Int, String?) -> Unit) {
        onSelectionChangedListener = object : OnSelectionChangedListener {
            override fun onSelectionChanged(position: Int, simpleMajorName: String?) {
                onSelectionChanged(position, simpleMajorName)
            }
        }
    }

    interface OnSelectionChangedListener {
        fun onSelectionChanged(position: Int, simpleMajorName: String?)
    }
}