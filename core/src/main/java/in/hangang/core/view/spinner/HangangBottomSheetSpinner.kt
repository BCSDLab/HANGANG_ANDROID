package `in`.hangang.core.view.spinner

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog

class HangangBottomSheetSpinner @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    val view: View = LayoutInflater.from(context).inflate(R.layout.layout_spinner, this, true)

    var position = -1
        set(value) {
            if (items.isNotEmpty() && value >= 0 && value <= items.size)
                _textViewSelectedItem.text = items[value]
            field = value
        }
    var items: List<CharSequence> = emptyList()
        set(value) {
            position = 0
            _textViewSelectedItem.text = if (value.isEmpty()) "" else value[position]

            field = value
        }
    private var _textViewSelectedItem: TextView

    val textViewSelectedItem: TextView get() = _textViewSelectedItem
    val selection get() = _textViewSelectedItem.text

    init {
        _textViewSelectedItem = view.findViewById(R.id.text_view_spinner_selected_item)

        this.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val bottomSheet = BottomSheetDialog(context, R.style.HangangBottomSheetDialogTheme)
        val bottomSheetView =
            LayoutInflater.from(context).inflate(R.layout.layout_spinner_bottom_sheet, null)
        bottomSheetView.findViewById<ListView>(R.id.spinner_bottom_sheet_list_view).apply {
            adapter =
                ArrayAdapter(context, R.layout.simple_text_item_1, android.R.id.text1, items)
            divider = null
            setOnItemClickListener { _, _, position, _ ->
                this@HangangBottomSheetSpinner.position = position
                bottomSheet.dismiss()
            }
        }
        bottomSheet.setContentView(bottomSheetView)
        bottomSheet.show()
    }
}