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

    private val adapter = RankingListAdapter()
    private lateinit var major: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        major = savedInstanceState?.getString("major") ?: "교양학부"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO get adapter from data source

        binding.recyclerViewRankingList.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewRankingList.adapter = adapter
    }

    companion object {
        fun newInstance(major : String) : RankingListFragment = RankingListFragment().apply {
            arguments = Bundle().apply {
                putString("major", major)
            }
        }
    }
}