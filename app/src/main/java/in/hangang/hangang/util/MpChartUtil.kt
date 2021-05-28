package `in`.hangang.hangang.util

import `in`.hangang.hangang.R
import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


fun BarChart.initScoreChart(context: Context, entries: ArrayList<BarEntry>) {

    var set = BarDataSet(entries, "DataSet")//데이터셋 초기화 하기
    set.color = ContextCompat.getColor(context, R.color.mango)

    val dataSet: ArrayList<IBarDataSet> = ArrayList()
    dataSet.add(set)
    val data = BarData(dataSet)

    data.barWidth = 0.97f//막대 너비 설정하기
    this.run {
        this.data = data //차트의 데이터를 data로 설정해줌.
        setFitBars(true)
        invalidate()
    }

    this.run {
        description.isEnabled = false //차트 옆에 별도로 표기되는 description이다. false로 설정하여 안보이게 했다.
        setMaxVisibleValueCount(10) // 최대 보이는 그래프 개수


        xAxis.run {
            position = XAxis.XAxisPosition.BOTTOM//X축을 아래에다가 둔다.
            setDrawAxisLine(true) // 축 그림
            setDrawGridLines(false) // 격자
            textColor = ContextCompat.getColor(context, R.color.gray_500) //라벨 색상
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    //return entries.get(value as Int).data as String
                    val values = arrayOf("", "1.0", "", "2.0", "", "3.0", "", "4.0", "", "5.0")
                    //return values[values.toInt()]
                    return values.getOrNull(value.toInt() - 1) ?: value.toString()
                }
            }

            textSize = 10f // 텍스트 크기
        }

        axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
        axisLeft.isEnabled = false
        setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
        legend.isEnabled = false //차트 범례 설정

    }
}
