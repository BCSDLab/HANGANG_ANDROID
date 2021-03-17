package `in`.hangang.core.view.timetable

import `in`.hangang.core.view.dp2Px
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.ColorInt

class TimetableColumnHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ViewGroup(context, attrs, defStyleAttr) {

    private val linePaint: Paint by lazy {
        Paint().apply {
            strokeWidth = dp2Px(1f).toFloat()
        }
    }

    private val textPaint = Paint()

    @ColorInt
    var dividerColor: Int = Color.LTGRAY
        set(value) {
            field = value
            linePaint.color = value
            invalidate()
        }

    @ColorInt
    var textColor: Int = Color.BLACK
        set(value) {
            field = value
            textPaint.color = textColor
            invalidate()
        }

    var textSize: Float = 12f
        set(value) {
            field = value
            textPaint.textSize = value
            invalidate()
        }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("Not yet implemented")
    }
}