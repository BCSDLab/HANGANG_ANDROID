package `in`.hangang.hangang.ui.home.ranking.adapter

import `in`.hangang.hangang.R
import `in`.hangang.hangang.ui.home.ranking.fragment.RankingListFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RankingPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fullMajors = fragmentActivity.resources.getStringArray(R.array.major_full)

    override fun getItemCount() = 9

    override fun createFragment(position: Int): Fragment {
        return RankingListFragment.newInstance(fullMajors[position])
    }

}