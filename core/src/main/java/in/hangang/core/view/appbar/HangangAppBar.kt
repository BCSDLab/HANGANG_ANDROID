package `in`.hangang.core.view.appbar

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class HangangAppBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    val view: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.layout_app_bar_base, null)
    }

    private var _textViewTitle: TextView
    private var _buttonBack: ImageButton
    private var _buttonRight: Button

    val textViewTitle: TextView get() = _textViewTitle
    val buttonBack: ImageButton get() = _buttonBack
    val buttonRight: Button get() = _buttonRight

    var hangangAppBarClickListener: HangangAppBarClickListener? = null
        set(value) {
            value?.apply {
                _buttonBack.setOnClickListener { onBackButtonClick(_buttonBack) }
                _buttonRight.setOnClickListener { onRightButtonClick(_buttonRight) }
            }
            field = value
        }

    init {
        addView(view)

        _textViewTitle = view.findViewById(R.id.app_bar_title)
        _buttonBack = view.findViewById(R.id.app_bar_back_button)
        _buttonRight = view.findViewById(R.id.app_bar_text_button)

        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.HangangAppBar,
            defStyleAttr,
            defStyleRes
        ).apply {
            _buttonBack.visibility =
                if (getBoolean(R.styleable.HangangAppBar_showBackButton, true))
                    View.VISIBLE
                else
                    View.GONE
            _buttonRight.visibility =
                if (getBoolean(R.styleable.HangangAppBar_showRightButton, false))
                    View.VISIBLE
                else
                    View.GONE

            _buttonRight.text = getString(R.styleable.HangangAppBar_rightButtonText)
            _textViewTitle.text = getString(R.styleable.HangangAppBar_titleText)
        }
    }
}