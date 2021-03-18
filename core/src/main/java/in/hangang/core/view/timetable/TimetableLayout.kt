package `in`.hangang.core.view.timetable

import `in`.hangang.core.R
import `in`.hangang.core.view.dp2Px
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.icu.util.Measure
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.annotation.ColorInt
import kotlin.math.roundToInt

class TimetableLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ViewGroup(context, attrs, defStyleAttr) {

    private val topBarHeight: Int by lazy { dp2Px(26f) }


    private val linePaint: Paint by lazy {
        Paint().apply {
            strokeWidth = dp2Px(1f).toFloat()
        }
    }

    private var cellWidth : Int = 0
    private var cellHeight : Int = 0

    var rowCount = 9
    set(value) {
        field = value
        postInvalidate()
    }
    var columnCount = 5
        set(value) {
            field = value
            postInvalidate()
        }

    var rowHeight = 0
        set(value) {
            field = value
            postInvalidate()
        }
    var columnWidth = 0
        set(value) {
            field = value
            postInvalidate()
        }

    @ColorInt
    var dividerColor: Int = Color.LTGRAY
        set(value) {
            field = value
            linePaint.color = value
            postInvalidate()
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
        val defaultWidth = MeasureSpec.getSize(widthMeasureSpec)
        val defaultHeight = MeasureSpec.getSize(heightMeasureSpec)

        val width =
                when (MeasureSpec.getMode(widthMeasureSpec)) {
                    MeasureSpec.EXACTLY -> defaultWidth
                    else -> columnCount * columnWidth
                }

        val height =
                when (MeasureSpec.getMode(heightMeasureSpec)) {
                    MeasureSpec.EXACTLY -> defaultHeight
                    else -> rowCount * rowHeight
                }

        cellWidth = width / columnCount
        cellHeight = height / rowCount

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams

            val desiredWidth = cellWidth * (lp.columnEnd - lp.columnStart)
            val desiredHeight = cellHeight * (lp.rowEnd - lp.rowStart)

            child.measure(
                    MeasureSpec.makeMeasureSpec(desiredWidth.toInt(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(desiredHeight.toInt(), MeasureSpec.EXACTLY)
            )
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams

            val left = cellWidth * lp.columnStart
            val right = cellWidth * lp.columnEnd
            val top = cellHeight * lp.rowStart
            val bottom = cellHeight * lp.rowEnd

            child.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            for(i in 0..columnCount) {
                it.drawLine(
                        (cellWidth * i).toFloat(),
                        0f,
                        (cellWidth * i).toFloat(),
                        measuredHeight.toFloat(),
                        linePaint
                )
            }
            for(i in 0..rowCount) {
                it.drawLine(
                        0f,
                        (cellHeight * i).toFloat(),
                        measuredWidth.toFloat(),
                        (cellHeight * i).toFloat(),
                        linePaint
                )
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