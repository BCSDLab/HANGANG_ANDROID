package `in`.hangang.core.view.appbar

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged

class SearchAppBar @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private val view = LayoutInflater.from(context).inflate(R.layout.layout_app_bar_search, this, true)

    var filterable : Filterable? = null
    var filterListener : Filter.FilterListener? = null
    
    val searchField = findViewById<EditText>(R.id.search_layout_edit_text).apply {
        addTextChangedListener {

        }
    }
    val searchButton = findViewById<ImageButton>(R.id.search_layout_button_search).apply { 
        setOnClickListener {
            filterable?.filter?.filter(searchField.text.toString(), filterListener)
        }
    }
}