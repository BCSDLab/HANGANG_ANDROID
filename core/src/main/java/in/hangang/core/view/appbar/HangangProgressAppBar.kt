package `in`.hangang.core.view.appbar

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*

class HangangProgressAppBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    val view: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.layout_app_bar_progress, null)
    }

    private var _progressBar: ProgressBar
    private var _textViewTitle: TextView
    private var _buttonBack: ImageButton
    private var _buttonRight: Button

    private val progressBar: ProgressBar get() = _progressBar
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

    var max : Int = 100
        set(value) {
            _progressBar.max = value
            field = value
        }

    var progress : Int = 0
        set(value) {
            _progressBar.progress = value
            field = value
        }

    init {
        val layoutParams = LayoutParams(context, attributeSet)
        layoutParams.width = LayoutParams.MATCH_PARENT
        view.layoutParams = layoutParams

        addView(view)

        _textViewTitle = view.findViewById(R.id.app_bar_title)
        _buttonBack = view.findViewById(R.id.app_bar_back_button)
        _buttonRight = view.findViewById(R.id.app_bar_text_button)
        _progressBar = view.findViewById(R.id.app_bar_progress)

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

        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.HangangProgressAppBar,
            defStyleAttr,
            defStyleRes
        ).apply {
            progress = getInteger(R.styleable.HangangProgressAppBar_progress, 0)
            max = getInteger(R.styleable.HangangProgressAppBar_max, 100)
        }
    }
}
