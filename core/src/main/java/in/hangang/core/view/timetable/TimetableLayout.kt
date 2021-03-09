package `in`.hangang.core.view.timetable

import `in`.hangang.core.R
import `in`.hangang.core.view.dp2Px
import `in`.hangang.core.view.sp2Px
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.annotation.ColorInt
import kotlin.math.roundToInt

class TimetableLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        const val MON = 0
        const val TUE = 1
        const val WED = 2
        const val THU = 3
        const val FRI = 4
    }

    private val topBarHeight: Int by lazy { dp2Px(26f) }
    private val timeHeight: Int by lazy { dp2Px(54f) }

    private val bound = Rect()

    private val linePaint: Paint by lazy {
        Paint().apply {
            strokeWidth = dp2Px(1f).toFloat()
        }
    }
    private val weekdayTextPaint: Paint by lazy {
        Paint()
    }
    private val timeTextPaint: Paint by lazy {
        Paint()
    }

    val startTime = 9   //09:00~
    val endTime = 17    //~18:00

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
            timeTextPaint.color = textColor
            weekdayTextPaint.color = textColor
            invalidate()
        }

    var textSize: Float = 12f
        set(value) {
            field = value
            timeTextPaint.textSize = value
            weekdayTextPaint.textSize = value
            invalidate()
        }

    init {
        /*
        <attr name="dividerColor" format="color|reference" />
        <attr name="startTime" format="integer|reference" />
        <attr name="endTime" format="integer|reference" />
        <attr name="dayOfWeeks" format="reference" />
         */

        setWillNotDraw(false)

        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.TimetableLayout,
                defStyleAttr,
                0
        ).apply {
            dividerColor = getColor(R.styleable.TimetableLayout_dividerColor, Color.parseColor("#eeeeee"))
            textColor = getColor(R.styleable.TimetableLayout_android_textColor, Color.BLACK)
            textSize = getDimensionPixelSize(R.styleable.TimetableLayout_android_textSize, sp2Px(12f)).toFloat()

            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultWidth = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val defaultHeight = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        val maxHeight = (endTime - startTime + 1) * timeHeight + topBarHeight

        val width =
                when (MeasureSpec.getMode(widthMeasureSpec)) {
                    MeasureSpec.UNSPECIFIED -> defaultWidth
                    MeasureSpec.AT_MOST -> defaultWidth
                    MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
                    else -> defaultWidth
                }

        val height =
                when (MeasureSpec.getMode(heightMeasureSpec)) {
                    MeasureSpec.UNSPECIFIED -> maxHeight
                    MeasureSpec.AT_MOST -> {
                        if (maxHeight > defaultHeight) defaultHeight
                        else maxHeight
                    }
                    MeasureSpec.EXACTLY -> defaultHeight
                    else -> defaultHeight
                }

        val childDesiredWidth = (width / 11.0 * 2).roundToInt()

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams

            val desiredHeight = ((lp.endTime - lp.startTime) * timeHeight).roundToInt()

            child.measure(
                    MeasureSpec.makeMeasureSpec(childDesiredWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY)
            )
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val cellWidth = (measuredWidth / 11.0).roundToInt()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams

            val left = cellWidth + cellWidth * lp.weekday * 2
            val right = cellWidth + cellWidth * (lp.weekday * 2 + 2)
            val top = (topBarHeight + (lp.startTime - startTime) * timeHeight).roundToInt()
            val bottom = (topBarHeight + (lp.endTime - startTime) * timeHeight).roundToInt()

            child.layout(left, top, right, bottom)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            val timeTextPaddingRight = dp2Px(6f)
            val timeTextPaddingTop = dp2Px(4f)
            val cellWidth = (measuredWidth / 11.0).roundToInt()
            it.drawLine(0F, topBarHeight.toFloat(), measuredWidth.toFloat(), topBarHeight.toFloat(), linePaint)
            for (i in 1..endTime - startTime + 1) {
                val text = String.format("%02d", i + startTime - 1)
                timeTextPaint.getTextBounds(text, 0, text.length, bound)
                it.drawText(text, (cellWidth - timeTextPaddingRight - bound.width()).toFloat(), (topBarHeight + timeHeight * (i - 1) + bound.height() + timeTextPaddingTop).toFloat(), timeTextPaint)
                it.drawLine(cellWidth.toFloat(), (topBarHeight + timeHeight * i).toFloat(), measuredWidth.toFloat(), (topBarHeight + timeHeight * i).toFloat(), linePaint)
            }
            for (i in 0 until 5) {
                val text = when(i) {
                    MON -> context.getString(R.string.timetable_mon)
                    TUE -> context.getString(R.string.timetable_tue)
                    WED -> context.getString(R.string.timetable_wed)
                    THU -> context.getString(R.string.timetable_thu)
                    FRI -> context.getString(R.string.timetable_fri)
                    else -> context.getString(R.string.timetable_mon)
                }
                weekdayTextPaint.getTextBounds(text, 0, text.length, bound)
                it.drawText(text, (cellWidth * (3 + 2 * i) - cellWidth - bound.width() * 0.5).toFloat(), (topBarHeight.toFloat() - bound.height() * 0.5).toFloat(), weekdayTextPaint)
                it.drawLine((cellWidth * (1 + 2 * i)).toFloat(), topBarHeight.toFloat(), (cellWidth * (1 + 2 * i)).toFloat(), measuredHeight.toFloat(), linePaint)
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(context)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    inner class LayoutParams(context: Context, attrs: AttributeSet? = null) : MarginLayoutParams(context, attrs) {

        var weekday: Int = MON
        var startTime: Float = 9f
        var endTime: Float = 11f

        constructor(context: Context, weekday: Int, startTime: Float, endTime: Float) : this(context, null) {
            this.weekday = weekday
            this.startTime = startTime
            this.endTime = endTime
        }

        init {
            context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.TimetableLayout,
                    0,
                    0
            ).apply {
                weekday = getInt(R.styleable.TimetableLayout_layout_weekday, MON)
                startTime = (getString(R.styleable.TimetableLayout_layout_startTime)
                        ?: "9").toFloat()
                endTime = (getString(R.styleable.TimetableLayout_layout_endTime) ?: "11").toFloat()

                recycle()
            }
        }

    }

}