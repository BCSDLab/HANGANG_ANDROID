package `in`.hangang.hangang.ui.home.ranking.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentRankingListBinding
import `in`.hangang.hangang.ui.home.ranking.adapter.RankingListAdapter
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

class RankingListFragment : ViewBindingFragment<FragmentRankingListBinding>() {
    override val layoutId = R.layout.fragment_ranking_list

    val adapter = RankingListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO get adapter from data source

        binding.recyclerViewRankingList.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewRankingList.adapter = adapter
    }
}