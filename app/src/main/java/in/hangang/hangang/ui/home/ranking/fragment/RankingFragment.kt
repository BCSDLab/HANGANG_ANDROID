package `in`.hangang.hangang.ui.home.ranking.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentHomeRankingBinding
import `in`.hangang.hangang.ui.home.ranking.adapter.RankingPagerAdapter
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator

class RankingFragment : ViewBindingFragment<FragmentHomeRankingBinding>() {
    override val layoutId = R.layout.fragment_home_ranking

    private val simpleMajor: Array<String> by lazy {
        requireActivity().resources.getStringArray(R.array.major_simple)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTab()
    }

    private fun initTab() {
        binding.rankingPager.adapter = RankingPagerAdapter(requireActivity())
        TabLayoutMediator(binding.rankingTab, binding.rankingPager) { tab, position ->
            tab.text = simpleMajor[position]
        }.attach()
    }
}
