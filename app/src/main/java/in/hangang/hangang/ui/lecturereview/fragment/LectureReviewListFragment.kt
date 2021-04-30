package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentListReviewLectureBinding
import `in`.hangang.hangang.ui.home.ranking.adapter.RankingListAdapter
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureReviewAdapter
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.util.LogUtil
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel


class LectureReviewListFragment : ViewBindingFragment<FragmentListReviewLectureBinding>() {
    override val layoutId: Int = R.layout.fragment_list_review_lecture
    private val lectureReviewListViewModel: LectureReviewListViewModel by viewModel()
    private val adapter = LectureReviewAdapter()
    private lateinit var major: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lectureReviewRecyclerview.adapter = adapter
        initViewModel()
        major = arguments?.getString("major").toString()
        LogUtil.e(major)
        getLectureReviewList("")
    }
    private fun initViewModel() {
        with(lectureReviewListViewModel) {
            rankingLectureList.observe(viewLifecycleOwner, {
                it?.let { adapter.submitData(lifecycle, it) }

            })

        }
    }
    private fun getLectureReviewList(major: String){
        lectureReviewListViewModel.getLectureReviewList(major)
    }



}