package `in`.hangang.hangang.ui.home.ranking.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.FragmentHomeRankingListBinding
import `in`.hangang.hangang.ui.home.ranking.adapter.RankingListAdapter
import `in`.hangang.hangang.ui.home.ranking.viewmodel.RankingLectureViewModel
import `in`.hangang.hangang.util.LogUtil
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.koin.androidx.viewmodel.ext.android.viewModel

class RankingListFragment : ViewBindingFragment<FragmentHomeRankingListBinding>() {
    override val layoutId = R.layout.fragment_home_ranking_list
    private val rankingLectureViewModel: RankingLectureViewModel by viewModel()
    private val adapter = RankingListAdapter()
    private lateinit var major: String
    private val navController: NavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private val rankingItemClickListener = object : RecyclerViewClickListener {
        override fun onClick(view: View, position: Int, item: Any) {
            goToRead(item as RankingLectureItem)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            major = it.getString("major").toString()
            rankingLectureViewModel.majorArrayList.clear()
            rankingLectureViewModel.majorArrayList.add(major)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewRankingList.adapter = adapter
        setUnDelieverableHandler()
        initViewModel()
        initEvent()
        getRankingList()

    }


    private fun getRankingList() {
        rankingLectureViewModel.getRankingLectureByTotalRating(rankingLectureViewModel.majorArrayList)
    }

    fun setUnDelieverableHandler() {
        RxJavaPlugins.setErrorHandler {
            if (it is UndeliverableException) {
                binding.failRankingListLinearlayout.visibility = View.VISIBLE
            }
        }

    }

    private fun initEvent() {
        binding.rankingRetryButton.setOnClickListener {
            getRankingList()
        }
    }

    private fun initViewModel() {
        adapter.setClickListener(rankingItemClickListener)
        with(rankingLectureViewModel) {
            rankingLectureList.observe(viewLifecycleOwner, {
                it?.let { adapter.submitList(it) }

            })

        }
    }
    private fun goToRead(item: RankingLectureItem) {
        val bundle = Bundle()
        bundle.putParcelable("lecture", item)
        navController.navigate(
            R.id.action_navigation_home_to_navigation_evaluation,
            bundle
        )
    }

    companion object {
        fun newInstance(major: String): RankingListFragment = RankingListFragment().apply {
            arguments = Bundle().apply {
                putString("major", major)
            }
        }
    }
}