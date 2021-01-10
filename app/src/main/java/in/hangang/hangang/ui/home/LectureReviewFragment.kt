package `in`.hangang.hangang.ui.home

import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentLectureReviewBinding
import `in`.hangang.hangang.util.initScoreChart
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.BarEntry


class LectureReviewFragment : Fragment() {

    protected lateinit var binding: FragmentLectureReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lecture_review, container, false
        )

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, 1.0f))
        entries.add(BarEntry(2f, 8.0f))
        entries.add(BarEntry(3f, 15.0f))
        entries.add(BarEntry(4f, 54.0f))
        entries.add(BarEntry(5f, 40.0f))
        entries.add(BarEntry(6f, 32.0f))
        entries.add(BarEntry(7f, 47.0f))
        entries.add(BarEntry(8f, 130.0f))
        entries.add(BarEntry(9f, 56.0f))
        entries.add(BarEntry(10f, 38.0f))

        binding.barchart.initScoreChart(requireContext(), entries)
    }


}
