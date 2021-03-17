package `in`.hangang.core.view.timetable

import `in`.hangang.core.R
import `in`.hangang.core.view.dp2Px
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

    private val topBarHeight: Int by lazy { dp2Px(26f) }

    private val bound = Rect()

    private val linePaint: Paint by lazy {
        Paint().apply {
            strokeWidth = dp2Px(1f).toFloat()
        }
    }

    var rowCount = 9
    var columnCount = 5

    var rowHeight = 0
    var columnWidth = 0

    @ColorInt
    var dividerColor: Int = Color.LTGRAY
        set(value) {
            field = value
            linePaint.color = value
            invalidate()
        }

    init {
        setWillNotDraw(false)

        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.TimetableLayout,
                defStyleAttr,
                0
        ).apply {
            dividerColor = getColor(R.styleable.TimetableLayout_dividerColor, Color.parseColor("#eeeeee"))
            rowCount = getInteger(R.styleable.TimetableLayout_rowCount, 9)
            columnCount = getInteger(R.styleable.TimetableLayout_columnCount, 5)
            rowHeight = getDimensionPixelSize(R.styleable.TimetableLayout_rowHeight, dp2Px(54f))
            columnWidth = getDimensionPixelSize(R.styleable.TimetableLayout_rowHeight, dp2Px(64f))

            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultWidth = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val defaultHeight = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        val maxHeight = rowCount * rowHeight
        val maxWidth = columnCount * columnWidth

        val width =
                when (MeasureSpec.getMode(widthMeasureSpec)) {
                    MeasureSpec.UNSPECIFIED -> {
                        if (columnWidth < 0) {
                            defaultWidth
                        } else {
                            maxWidth
                        }
                    }
                    MeasureSpec.AT_MOST -> {
                        if (maxWidth > defaultWidth) defaultWidth
                        else maxWidth
                    }
                    MeasureSpec.EXACTLY -> defaultWidth
                    else -> defaultWidth
                }

        val height =
                when (MeasureSpec.getMode(heightMeasureSpec)) {
                    MeasureSpec.UNSPECIFIED -> {
                        if (rowHeight < 0) {
                            defaultHeight
                        } else {
                            maxHeight
                        }
                    }
                    MeasureSpec.AT_MOST -> {
                        if (maxHeight > defaultHeight) defaultHeight
                        else maxHeight
                    }
                    MeasureSpec.EXACTLY -> defaultHeight
                    else -> defaultHeight
                }

        val childDesiredWidthUnit = if (columnWidth < 0) {
            width / columnCount
        } else {
            columnWidth
        }

        val childDesiredHeightUnit = if (rowHeight < 0) {
            height / rowHeight
        } else {
            rowHeight
        }

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams

            val desiredWidth = childDesiredWidthUnit * (lp.columnEnd - lp.columnStart)
            val desiredHeight = childDesiredHeightUnit * (lp.rowEnd - lp.rowStart)

            child.measure(
                    MeasureSpec.makeMeasureSpec(desiredWidth.toInt(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(desiredHeight.toInt(), MeasureSpec.EXACTLY)
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
                val text = when (i) {
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
        var rowStart: Float = 0f
        var rowEnd: Float = 0f
        var columnStart: Float = 0f
        var columnEnd: Float = 0f

        constructor(
                rowStart: Float,
                rowEnd: Float,
                columnStart: Float,
                columnEnd: Float,
        ) : this(context, null) {
            this.rowStart = rowStart
            this.rowEnd = rowEnd
            this.columnStart = columnStart
            this.columnEnd = columnEnd
        }

        init {
            context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.TimetableLayout,
                    0,
                    0
            ).apply {
                rowStart = getFloat(R.styleable.TimetableLayout_layout_rowStart, 0f)
                rowEnd = getFloat(R.styleable.TimetableLayout_layout_rowEnd, 0f)
                columnStart = getFloat(R.styleable.TimetableLayout_layout_columnStart, 0f)
                columnEnd = getFloat(R.styleable.TimetableLayout_layout_columnEnd, 0f)

                recycle()
            }
        }
    }
}