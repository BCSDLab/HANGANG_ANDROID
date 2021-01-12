package `in`.hangang.core.view.appbar

import `in`.hangang.core.R
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat

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

    val progressBar = ProgressBar(context, attributeSet, R.style.Widget_AppCompat_ProgressBar_Horizontal).apply {
        layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        progressDrawable = ContextCompat.getDrawable(context, R.drawable.app_bar_progress)
    }

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
        dividerContainer.addView(progressBar)
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