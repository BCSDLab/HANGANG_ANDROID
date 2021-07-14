package `in`.hangang.core.view.appbar

import `in`.hangang.core.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*

class SearchAppBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private val view =
        LayoutInflater.from(context).inflate(R.layout.layout_app_bar_search, this, true)

    interface SearchListener {
        fun onSearch(keyword: String)
    }

    var filterable: Filterable? = null
    var filterListener: Filter.FilterListener? = null
    var searchListener: SearchListener? = null

    var onBackButtonClickListener: View.OnClickListener? = null

    val searchField = findViewById<EditText>(R.id.search_layout_edit_text).apply {
        setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                search()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                Log.e("focus","${hasFocus}")
                showBackButton = hasFocus
            }
        }
    }
    val searchButton = findViewById<ImageButton>(R.id.search_layout_button_search).apply {
        setOnClickListener {
            search()
        }
    }
    val backButton = findViewById<ImageButton>(R.id.app_bar_back_button).apply {
        setOnClickListener {
            onBackButtonClickListener?.onClick(it)
        }
    }

    var showBackButton = true
        set(value) {
            backButton.visibility = if (value) View.VISIBLE else View.GONE
            field = value
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
            showBackButton = getBoolean(R.styleable.SearchAppBar_showBackButton, true)

            recycle()
        }
    }

    fun search() {
        if (searchListener != null)
            searchListener!!.onSearch(searchField.text.toString())
        else
            filterable?.filter?.filter(searchField.text.toString(), filterListener)
    }

    inline fun setSearchListener(crossinline searchListener: (String) -> Unit) {
        this.searchListener = object : SearchListener {
            override fun onSearch(keyword: String) {
                searchListener(keyword)
            }
        }
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

    fun addTextChangedListener(textWatcher: TextWatcher) =
        searchField.addTextChangedListener(textWatcher)

    fun removeTextChangedListener(textWatcher: TextWatcher) =
        searchField.removeTextChangedListener(textWatcher)
}