package `in`.hangang.core.view.appbar

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ProgressBar

class ProgressAppBar @JvmOverloads constructor(
    context: Context,
    private val attributeSet: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
    private val defStyleRes: Int = 0
) : BaseAppBar(context, attributeSet, defStyleAttr, defStyleRes) {
    override var showDivider = false
        set(value) {
            super.showDivider = false
            field = value
        }

    val progressBar = LayoutInflater.from(context)
        .inflate(R.layout.layout_app_bar_progress, dividerContainer, true)
        .findViewById<ProgressBar>(R.id.app_bar_progress)

    var max = 100
        set(value) {
            progressBar.max = value
            field = value
        }

    var progress = 0
        set(value) {
            progressBar.progress = value
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ProgressAppBar,
            defStyleAttr,
            defStyleRes
        ).apply {
            max = getInteger(R.styleable.ProgressAppBar_max, 100)
            progress = getInteger(R.styleable.ProgressAppBar_progress, 0)
        }
    }
}