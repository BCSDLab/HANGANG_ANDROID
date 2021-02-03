package `in`.hangang.hangang.ui.home.recentlectures.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentHomeRecentLecturesBinding
import `in`.hangang.hangang.ui.home.recentlectures.adapter.RecentLecturesRecyclerViewAdapter
import `in`.hangang.hangang.ui.home.recentlectures.viewmodel.RecentLecturesFragmentViewModel
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecentLecturesFragment : ViewBindingFragment<FragmentHomeRecentLecturesBinding>() {
    override val layoutId = R.layout.fragment_home_recent_lectures

    private val recentLecturesFragmentViewModel: RecentLecturesFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewRecentLectures.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewRecentLectures.adapter = RecentLecturesRecyclerViewAdapter()

        recentLecturesFragmentViewModel.timetableCount.observe(viewLifecycleOwner) {
            binding.recyclerViewEmpty.visibility = if (it == 0) View.VISIBLE else View.GONE
        }
    }
}