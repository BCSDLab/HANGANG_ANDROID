package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.evaluation.Chart
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.FragmentLectureReviewDetailBinding
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureClassTimeAdapter
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewDetailViewModel
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.initScoreChart
import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.github.mikephil.charting.data.BarEntry
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureReviewDetailFragment : ViewBindingFragment<FragmentLectureReviewDetailBinding>() {
    override val layoutId = R.layout.fragment_lecture_review_detail
    lateinit var lecture: RankingLectureItem
    private val lectureClassTimeAdapter = LectureClassTimeAdapter()
    private val navController: NavController by lazy {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
    }
    private val lectureReviewDetailViewModel: LectureReviewDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lecture = arguments?.getParcelable<RankingLectureItem>("lecture")!!
        binding.lecture = lecture
        //lecture?.id?.let { lectureReviewDetailViewModel.getEvaluationRating(it) }
        init()
        initViewModel()
    }
    fun initViewModel(){
        with(lectureReviewDetailViewModel){
            classLectureList.observe(viewLifecycleOwner,{
                it?.let { lectureClassTimeAdapter.submitList(it) }
            })
            chartList.observe(viewLifecycleOwner,{
                binding.lectureDetailBarchart.initScoreChart(requireContext(), it)
            })
            evaluationTotal.observe(viewLifecycleOwner,{
                it?.let {
                    LogUtil.e("success")
                    binding.evaluation = it }
            })
        }
    }


    fun init(){
        binding.lectureDetailClassTimeRecyclerview.adapter = lectureClassTimeAdapter
        lecture.id.let {
            lectureReviewDetailViewModel.getClassLectureList(it)
            lectureReviewDetailViewModel.getEvaluationRating(it)
            lectureReviewDetailViewModel.getEvaluationTotal(it)
        }
        /*
        var list: ArrayList<BarEntry> = ArrayList()
        list.add(BarEntry(1f, 30f))
        list.add(BarEntry(2f, 10f))
        list.add(BarEntry(3f, 20f))
        list.add(BarEntry(4f, 30f))
        list.add(BarEntry(5f, 40f))
        list.add(BarEntry(6f, 50f))
        list.add(BarEntry(7f, 60f))
        list.add(BarEntry(8f, 70f))
        list.add(BarEntry(9f, 80f))
        list.add(BarEntry(10f, 90f))
        binding.lectureDetailBarchart.initScoreChart(requireContext(), list)

         */

    }
}
