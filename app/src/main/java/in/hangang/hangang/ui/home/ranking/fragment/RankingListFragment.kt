package `in`.hangang.hangang.ui.home.ranking.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentHomeRankingListBinding
import `in`.hangang.hangang.ui.home.ranking.adapter.RankingListAdapter
import `in`.hangang.hangang.ui.home.ranking.viewmodel.RankingLectureViewModel
import `in`.hangang.hangang.util.LogUtil
import android.os.Bundle
import android.view.View
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.koin.androidx.viewmodel.ext.android.viewModel

class RankingListFragment : ViewBindingFragment<FragmentHomeRankingListBinding>() {
    override val layoutId = R.layout.fragment_home_ranking_list
    private val rankingLectureViewModel: RankingLectureViewModel by viewModel()
    private val adapter = RankingListAdapter()
    private lateinit var major: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            major = it.getString("major").toString()
        }
        //major = "" ?: requireActivity().resources.getStringArray(R.array.major_full)[0]
        LogUtil.e(major)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RxJavaPlugins.setErrorHandler {
            if (it is UndeliverableException) {
                LogUtil.e("undeliver")
            }
        }
        binding.recyclerViewRankingList.adapter = adapter

        initViewModel()
        getRankingList(major)

    }

    private fun getRankingList(major: String) {
        rankingLectureViewModel.getRankingLectureByTotalRating(major)
    }

    private fun initViewModel() {
        with(rankingLectureViewModel) {
            rankingLectureList.observe(viewLifecycleOwner, {
                it?.let { adapter.submitList(it) }

            })
        }
    }

    companion object {
        fun newInstance(major: String): RankingListFragment = RankingListFragment().apply {
            arguments = Bundle().apply {
                putString("major", major)
            }
        }
    }
}