package `in`.hangang.hangang.view.timetable

import `in`.hangang.core.R
import `in`.hangang.core.view.dp2Px
import `in`.hangang.core.view.setVisibility
import `in`.hangang.core.view.sp2Px
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.util.timetable.TimetableUtil
import `in`.hangang.hangang.util.toValuesList
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

class TimetableLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) :
        ViewGroup(context, attrs, defStyleAttr) {

    interface RowHeaderTextFormatter {
        fun format(rowPosition: Int): String
    }

    interface ColumnHeaderTextFormatter {
        fun format(columnPosition: Int): String
    }

    interface TimetableItemClickListener {
        fun onTimetableItemClick(view: View, lectureTimeTable: LectureTimeTable)
    }

    interface ScrollViewCallback {
        fun scrollToItemView(timetableItemViews: List<View>)
        fun scrollToDummyView(timetableDummyViews: List<View>)
    }

    data class RC(
            val rowStart: Float,
            val rowEnd: Float,
            val columnStart: Float,
            val columnEnd: Float
    )

    private val timetableItemMap = mutableMapOf<LectureTimeTable, List<View>>()
    private val timetableDummyItemMap = mutableMapOf<LectureTimeTable, List<View>>()

    private var itemAddedCount = 0

    private val linePaint: Paint by lazy {
        Paint().apply {
            strokeWidth = dp2Px(1f).toFloat()
        }
    }

    private val columnHeaderTextPaint = Paint()
    private val rowHeaderTextPaint = Paint()

    private var cellWidth: Int = 0
    private var cellHeight: Int = 0

    var rowHeaderTextFormatter = object : RowHeaderTextFormatter {
        override fun format(rowPosition: Int): String {
            return String.format("%02d", rowPosition + 9)
        }
    }

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

    var timetableItemClickListener : TimetableItemClickListener? = null
    var scrollViewCallback : ScrollViewCallback? = null

    var isShowingDummyView = true
        set(value) {
            timetableDummyItemMap.toValuesList().forEach {
                it.setVisibility(value)
            }
            field = value
        }

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
    var columnHeaderHeight = 0
        set(value) {
            field = value
            postInvalidate()
        }
    var rowHeaderWidth = 0
        set(value) {
            field = value
            postInvalidate()
        }

    @ColorInt
    var rowHeaderTextColor = Color.BLACK
        set(value) {
            field = value
            rowHeaderTextPaint.color = value
            postInvalidate()
        }

    @ColorInt
    var columnHeaderTextColor = Color.BLACK
        set(value) {
            field = value
            columnHeaderTextPaint.color = value
            postInvalidate()
        }
    var rowHeaderTextSize = 0
        set(value) {
            field = value
            rowHeaderTextPaint.textSize = value.toFloat()
            postInvalidate()
        }
    var columnHeaderTextSize = 0
        set(value) {
            field = value
            columnHeaderTextPaint.textSize = value.toFloat()
            postInvalidate()
        }

    @ColorInt
    var dividerColor: Int = Color.LTGRAY
        set(value) {
            field = value
            linePaint.color = value
            postInvalidate()
        }

    private val textRectTemp = Rect()

    init {
        setWillNotDraw(false)

        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.TimetableLayout,
                defStyleAttr,
                0
        ).apply {
            dividerColor =
                    getColor(R.styleable.TimetableLayout_dividerColor, Color.parseColor("#eeeeee"))
            rowCount = getInteger(R.styleable.TimetableLayout_rowCount, 9)
            columnCount = getInteger(R.styleable.TimetableLayout_columnCount, 5)
            rowHeight = getDimensionPixelSize(R.styleable.TimetableLayout_rowHeight, dp2Px(54f))
            columnWidth = getDimensionPixelSize(R.styleable.TimetableLayout_rowHeight, dp2Px(64f))
            columnHeaderHeight =
                    getDimensionPixelSize(R.styleable.TimetableLayout_topHeaderHeight, dp2Px(36f))
            rowHeaderWidth =
                    getDimensionPixelSize(R.styleable.TimetableLayout_leftHeaderWidth, dp2Px(36f))
            rowHeaderTextColor = getColor(
                    R.styleable.TimetableLayout_rowHeaderTextColor,
                    Color.parseColor("#222222")
            )
            columnHeaderTextColor = getColor(
                    R.styleable.TimetableLayout_columnHeaderTextColor,
                    Color.parseColor("#222222")
            )
            rowHeaderTextSize =
                    getDimensionPixelSize(R.styleable.TimetableLayout_rowHeaderTextSize, sp2Px(10f))
            columnHeaderTextSize =
                    getDimensionPixelSize(R.styleable.TimetableLayout_columnHeaderTextSize, sp2Px(12f))

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

        cellWidth = (width - rowHeaderWidth) / columnCount
        cellHeight = (height - columnHeaderHeight) / rowCount

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

            val left = cellWidth * lp.columnStart + rowHeaderWidth
            val right = cellWidth * lp.columnEnd + rowHeaderWidth
            val top = cellHeight * lp.rowStart + columnHeaderHeight
            val bottom = cellHeight * lp.rowEnd + columnHeaderHeight

            child.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawLine(
                    0f,
                    columnHeaderHeight.toFloat(),
                    measuredWidth.toFloat(),
                    columnHeaderHeight.toFloat(),
                    linePaint
            ) // Draw a top header line
            it.drawLine(
                    rowHeaderWidth.toFloat(),
                    columnHeaderHeight.toFloat(),
                    rowHeaderWidth.toFloat(),
                    measuredHeight.toFloat(),
                    linePaint
            ) //Draw a left header line
            for (i in 0 until columnCount) {
                with(columnHeaderTextFormatter.format(i)) {
                    columnHeaderTextPaint.getTextBounds(this, 0, this.length, textRectTemp)
                    it.drawText(
                            this,
                            (cellWidth * (i + 0.5)).toFloat() + rowHeaderWidth - textRectTemp.width() / 2,
                            (columnHeaderHeight - dp2Px(COLUMN_HEADER_TEXT_BOTTOM_MARGIN_DP) - textRectTemp.height() - textRectTemp.top).toFloat(),
                            columnHeaderTextPaint
                    )
                }
                it.drawLine(
                        (cellWidth * i).toFloat() + rowHeaderWidth,
                        columnHeaderHeight.toFloat(),
                        (cellWidth * i).toFloat() + rowHeaderWidth,
                        measuredHeight.toFloat(),
                        linePaint
                )
            } //Draw a column line
            for (i in 0 until rowCount) {
                with(rowHeaderTextFormatter.format(i)) {
                    rowHeaderTextPaint.getTextBounds(this, 0, this.length, textRectTemp)
                    it.drawText(
                            this,
                            (rowHeaderWidth - dp2Px(ROW_HEADER_TEXT_RIGHT_MARGIN_DP) - textRectTemp.width() - textRectTemp.left).toFloat(),
                            (columnHeaderHeight + cellHeight * i).toFloat() - textRectTemp.top + dp2Px(ROW_HEADER_TEXT_TOP_MARGIN_DP),
                            columnHeaderTextPaint
                    )
                }
                it.drawLine(
                        rowHeaderWidth.toFloat(),
                        (cellHeight * i).toFloat() + columnHeaderHeight,
                        measuredWidth.toFloat(),
                        (cellHeight * i).toFloat() + columnHeaderHeight,
                        linePaint
                )
            } //Draw a row line
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

    fun addTimetableItem(
            vararg lectureTimeTables: LectureTimeTable
    ) {
        lectureTimeTables.forEach { lectureTimeTable ->
            val views = mutableListOf<TextView>()
            TimetableUtil.convertApiExpressionToRC(lectureTimeTable.classTime
                    ?: "[]").forEach { rc ->
                views.add(TextView(context).apply {
                    setBackgroundColor(ContextCompat.getColor(context, TimetableUtil.timetableColors[itemAddedCount]))
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    text = String.format("%s\n%s %s", lectureTimeTable.name, lectureTimeTable.classNumber, lectureTimeTable.professor)
                    setTextSize(COMPLEX_UNIT_SP, 10f)
                    setPadding(
                            dp2Px(4f),
                            dp2Px(4f),
                            dp2Px(4f),
                            dp2Px(4f)
                    )
                    layoutParams = LayoutParams(
                            context, null,
                            rc.rowStart,
                            rc.rowEnd,
                            rc.columnStart,
                            rc.columnEnd
                    )
                    setOnClickListener {
                        timetableItemClickListener?.onTimetableItemClick(it, lectureTimeTable)
                    }
                })
            }
            replaceView(
                    map = timetableItemMap,
                    key = lectureTimeTable,
                    views = views
            )
            itemAddedCount++
        }

        scrollViewCallback?.scrollToItemView(timetableItemMap.toValuesList())
    }

    fun addTimetableDummyItem(
            vararg lectureTimeTables: LectureTimeTable
    ) {
        lectureTimeTables.forEach { lectureTimeTable ->
            val views = mutableListOf<View>()
            TimetableUtil.convertApiExpressionToRC(lectureTimeTable.classTime
                    ?: "[]").forEach { rc ->
                views.add(View(context).apply {
                    setBackgroundResource(R.drawable.background_timetable_outline)
                    layoutParams = LayoutParams(
                            context, null,
                            rc.rowStart,
                            rc.rowEnd,
                            rc.columnStart,
                            rc.columnEnd
                    )
                })
            }

            replaceView(
                    map = timetableDummyItemMap,
                    key = lectureTimeTable,
                    views = views
            )
        }
        scrollViewCallback?.scrollToDummyView(timetableDummyItemMap.toValuesList())
    }

    fun removeTimetableItem(
            vararg lectureTimeTables: LectureTimeTable
    ) {
        lectureTimeTables.forEach { lectureTimeTable ->
            timetableItemMap[lectureTimeTable]?.forEach {
                removeView(it)
            }
            timetableItemMap.remove(lectureTimeTable)
        }
    }

    fun removeTimetableDummyItem(
            vararg lectureTimeTables: LectureTimeTable
    ) {
        lectureTimeTables.forEach { lectureTimeTable ->
            timetableDummyItemMap[lectureTimeTable]?.forEach {
                removeView(it)
            }
            timetableDummyItemMap.remove(lectureTimeTable)
        }
    }

    fun removeAllTimetableItems() {
        timetableItemMap.toValuesList().forEach { removeView(it) }
        timetableItemMap.clear()
        itemAddedCount = 0
    }

    fun removeAllTimetableDummyItems() {
        timetableDummyItemMap.toValuesList().forEach { removeView(it) }
        timetableDummyItemMap.clear()
    }

    inline fun setTimetableItemClickListener(crossinline listener : (View, LectureTimeTable) -> Unit) {
        timetableItemClickListener = object : TimetableItemClickListener {
            override fun onTimetableItemClick(view: View, lectureTimeTable: LectureTimeTable) {
                listener(view, lectureTimeTable)
            }
        }
    }

    inline fun setScrollViewCallback(crossinline callbackItem: (List<View>) -> Unit, crossinline callbackDummy: (List<View>) -> Unit) {
        scrollViewCallback = object : ScrollViewCallback {
            override fun scrollToItemView(timetableItemViews: List<View>) {
                callbackItem(timetableItemViews)
            }

            override fun scrollToDummyView(timetableDummyViews: List<View>) {
                callbackDummy(timetableDummyViews)
            }
        }
    }

    private fun replaceView(map: MutableMap<LectureTimeTable, List<View>>, key: LectureTimeTable, views: List<View>) {
        map[key]?.forEach {
            removeView(it)
        }
        views.forEach { addView(it) }
        map[key] = views
    }

    class LayoutParams(context: Context, attrs: AttributeSet? = null) : MarginLayoutParams(
            WRAP_CONTENT, WRAP_CONTENT
    ) {
        var rowStart: Float = 0f
        var rowEnd: Float = 0f
        var columnStart: Float = 0f
        var columnEnd: Float = 0f

        constructor(
                context: Context,
                attrs: AttributeSet? = null,
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

    companion object {
        private const val ROW_HEADER_TEXT_RIGHT_MARGIN_DP = 6f
        private const val ROW_HEADER_TEXT_TOP_MARGIN_DP = 6f
        private const val COLUMN_HEADER_TEXT_BOTTOM_MARGIN_DP = 8f
    }
}