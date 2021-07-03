package `in`.hangang.hangang.ui.home.recentlectures.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.FragmentHomeRecentLecturesBinding
import `in`.hangang.hangang.ui.home.ranking.adapter.RankingListAdapter
import `in`.hangang.hangang.ui.home.recentlectures.adapter.RecentLecturesRecyclerViewAdapter
import `in`.hangang.hangang.ui.home.recentlectures.viewmodel.RecentLecturesFragmentViewModel
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecentLecturesFragment : ViewBindingFragment<FragmentHomeRecentLecturesBinding>() {
    override val layoutId = R.layout.fragment_home_recent_lectures

    private val recentLecturesFragmentViewModel: RecentLecturesFragmentViewModel by viewModel()
    private val rankingListAdapter = RankingListAdapter()
    private val navController: NavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private val recentlyReadLectureClickListener = object : RecyclerViewClickListener{
        override fun onClick(view: View, position: Int, item: Any) {
            goToRead(item as RankingLectureItem)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        subscribe()

    }

    private fun initView() {
        binding.recyclerViewRecentLectures.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewRecentLectures.adapter = rankingListAdapter
        rankingListAdapter.setClickListener(recentlyReadLectureClickListener)
        recentLecturesFragmentViewModel.getLectureList()

    }

    private fun subscribe() {
        recentLecturesFragmentViewModel.lectureList.observe(viewLifecycleOwner, {
            it.let {
                if (it.isEmpty()) {
                    binding.recyclerViewEmpty.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewEmpty.visibility = View.GONE
                }
                rankingListAdapter.submitList(it)
            }
        })
    }
    private fun goToRead(item: RankingLectureItem) {
        val bundle = Bundle()
        bundle.putParcelable("lecture", item)
        navController.navigate(R.id.navigation_evaluation,
            bundle
        )
    }
}