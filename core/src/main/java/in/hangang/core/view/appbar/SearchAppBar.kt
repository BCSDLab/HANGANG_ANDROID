package `in`.hangang.core.view.appbar

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.*

class SearchAppBar @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private val view = LayoutInflater.from(context).inflate(R.layout.layout_app_bar_search, this, true)

    var filterable: Filterable? = null
    var filterListener: Filter.FilterListener? = null

    val searchField = findViewById<EditText>(R.id.search_layout_edit_text).apply {
        setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                search()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }
    val searchButton = findViewById<ImageButton>(R.id.search_layout_button_search).apply {
        setOnClickListener {
            search()
        }
    }

    fun search() {
        filterable?.filter?.filter(searchField.text.toString(), filterListener)
    }
}