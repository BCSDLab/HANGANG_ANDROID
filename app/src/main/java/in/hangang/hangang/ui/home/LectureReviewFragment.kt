package `in`.hangang.hangang.ui.home

import `in`.hangang.hangang.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


abstract class LectureReviewFragment<B : ViewDataBinding, VM : LectureReviewViewModel> :
    Fragment() {

    protected lateinit var binding: B
    protected abstract val viewModel: VM

    private val chart: BarChart? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lecture_review, container, false
        )


        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1.2f,1.0f))
        entries.add(BarEntry(2.2f,8.0f))
        entries.add(BarEntry(3.2f,15.0f))
        entries.add(BarEntry(4.2f,54.0f))
        entries.add(BarEntry(5.2f,40.0f))
        entries.add(BarEntry(6.2f,32.0f))
        entries.add(BarEntry(7.2f,47.0f))
        entries.add(BarEntry(8.2f,130.0f))
        entries.add(BarEntry(9.2f,56.0f))
        entries.add(BarEntry(10.2f,38.0f))

        chart?.run {
            setMaxVisibleValueCount(10)
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM//X축을 아래에다가 둔다.
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(context,R.color.mango) //라벨 색상
                valueFormatter = MyXAxisFormatter() // 축 라벨 값 바꿔주기 위함
                textSize = 14f // 텍스트 크기
            }
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
            legend.isEnabled = false //차트 범례 설정

        }

        return binding.root

    }

    inner class MyXAxisFormatter : ValueFormatter(){
        private val days = arrayOf(" ","1.0"," ","2.0"," ","3.0"," ", "4.0", " ", "5.0")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }

}
