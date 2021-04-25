package `in`.hangang.hangang.view.timetable

import `in`.hangang.core.R
import `in`.hangang.core.view.dp2Px
import `in`.hangang.core.view.sp2Px
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt

class TimetableColumnHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    interface ColumnHeaderTextFormatter {
        fun format(columnPosition: Int): String
    }

    private val linePaint: Paint by lazy {
        Paint().apply {
            strokeWidth = dp2Px(1f).toFloat()
        }
    }
    private val columnHeaderTextPaint = Paint()

    private var cellWidth: Int = 0

    var columnHeaderTextFormatter = object : ColumnHeaderTextFormatter {
        override fun format(columnPosition: Int): String {
            return with(
                arrayOf(
                    context.getString(R.string.mon),
                    context.getString(R.string.tue),
                    context.getString(R.string.wed),
                    context.getString(R.string.thu),
                    context.getString(R.string.fri),
                    context.getString(R.string.sat),
                    context.getString(R.string.sun)
                )
            ) {
                this[columnPosition % this.size]
            }
        }
    }

    var columnCount = 5
        set(value) {
            field = value
            postInvalidate()
        }

    var columnHeaderHeight = 0
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
    var columnHeaderTextColor = Color.BLACK
        set(value) {
            field = value
            columnHeaderTextPaint.color = value
            postInvalidate()
        }

    @ColorInt
    var dividerColor: Int = Color.LTGRAY
        set(value) {
            field = value
            linePaint.color = value
            postInvalidate()
        }

    var columnHeaderTextSize = 0
        set(value) {
            field = value
            columnHeaderTextPaint.textSize = value.toFloat()
            postInvalidate()
        }

    private val textRectTemp = Rect()

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            `in`.hangang.hangang.R.styleable.TimetableColumnHeader,
            defStyleAttr,
            0
        ).apply {
            dividerColor =
                getColor(
                    `in`.hangang.hangang.R.styleable.TimetableColumnHeader_dividerColor,
                    Color.parseColor("#eeeeee")
                )
            columnCount = getInteger(`in`.hangang.hangang.R.styleable.TimetableColumnHeader_columnCount, 5)
            columnWidth =
                getDimensionPixelSize(`in`.hangang.hangang.R.styleable.TimetableColumnHeader_columnWidth, dp2Px(64f))
            columnHeaderHeight =
                getDimensionPixelSize(
                    `in`.hangang.hangang.R.styleable.TimetableColumnHeader_columnHeaderHeight,
                    dp2Px(36f)
                )
            columnHeaderTextColor = getColor(
                `in`.hangang.hangang.R.styleable.TimetableColumnHeader_columnHeaderTextColor,
                Color.parseColor("#222222")
            )
            columnHeaderTextSize =
                getDimensionPixelSize(
                    `in`.hangang.hangang.R.styleable.TimetableColumnHeader_columnHeaderTextSize,
                    sp2Px(12f)
                )

            recycle()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultWidth = MeasureSpec.getSize(widthMeasureSpec)
        val defaultHeight = MeasureSpec.getSize(heightMeasureSpec)

        columnHeaderTextPaint.getTextBounds("A", 0, 1, textRectTemp)

        val width =
            when (MeasureSpec.getMode(widthMeasureSpec)) {
                MeasureSpec.EXACTLY -> defaultWidth
                else -> columnCount * columnWidth
            }

        val height =
            when (MeasureSpec.getMode(heightMeasureSpec)) {
                MeasureSpec.EXACTLY -> defaultHeight
                else -> textRectTemp.height() + paddingTop + paddingBottom
            }

        cellWidth = (width - paddingLeft - paddingRight) / columnCount

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        for (i in 0 until columnCount) {
            with(columnHeaderTextFormatter.format(i)) {
                columnHeaderTextPaint.getTextBounds(this, 0, this.length, textRectTemp)
                canvas.drawText(
                    this,
                    paddingLeft + (cellWidth * (i + 0.5)).toFloat() - textRectTemp.width() / 2,
                    (dp2Px(COLUMN_HEADER_TEXT_TOP_MARGIN_DP) + textRectTemp.height() + textRectTemp.top).toFloat(),
                    columnHeaderTextPaint
                )
            }
        }//Draw a column line
        canvas.drawLine(
            0f,
            measuredHeight.toFloat() - linePaint.strokeWidth,
            measuredWidth.toFloat(),
            measuredHeight.toFloat() - linePaint.strokeWidth,
            linePaint
        ) // Draw a top header line

    }

    companion object {
        private const val COLUMN_HEADER_TEXT_TOP_MARGIN_DP = 12f
    }
}