package `in`.hangang.core.view.appbar

import `in`.hangang.core.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.*

class SearchAppBar @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
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

    init {
        context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.SearchAppBar,
                defStyleAttr,
                defStyleRes
        ).apply {
            searchField.hint = getString(R.styleable.SearchAppBar_android_hint)
            searchField.setText(getString(R.styleable.SearchAppBar_android_text))

            recycle()
        }
    }

    fun search() {
        filterable?.filter?.filter(searchField.text.toString(), filterListener)
    }

    //This is editText methods
    var text: Editable
        set(value) {
            searchField.text = value
        }
        get() = searchField.text

    val selectionStart get() = searchField.selectionStart
    val selectionEnd get() = searchField.selectionEnd

    fun setText(s: CharSequence) {
        searchField.setText(s)
    }

    fun setSelection(start: Int, end: Int) = searchField.setSelection(start, end)
    fun setSelection(index: Int) = searchField.setSelection(index)
    fun extendSelection(index: Int) = searchField.extendSelection(index)
    fun selectAll() = searchField.selectAll()

    fun addTextChangedListener(textWatcher: TextWatcher) = searchField.addTextChangedListener(textWatcher)
    fun removeTextChangedListener(textWatcher: TextWatcher) = searchField.removeTextChangedListener(textWatcher)
}