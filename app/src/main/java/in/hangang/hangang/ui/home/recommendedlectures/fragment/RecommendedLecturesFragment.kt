package `in`.hangang.hangang.ui.home.recommendedlectures.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentHomeRecommendedLecturesBinding
import `in`.hangang.hangang.ui.home.recommendedlectures.adapter.RecommendedLecturesRecyclerViewAdapter
import `in`.hangang.hangang.ui.home.recommendedlectures.viewmodel.RecommendedLecturesFragmentViewModel
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecommendedLecturesFragment : ViewBindingFragment<FragmentHomeRecommendedLecturesBinding>() {
    override val layoutId = R.layout.fragment_home_recommended_lectures

    private val recommendedLecturesFragmentViewModel: RecommendedLecturesFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewRecommendedLectures.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewRecommendedLectures.adapter = RecommendedLecturesRecyclerViewAdapter()

        recommendedLecturesFragmentViewModel.timetableCount.observe(viewLifecycleOwner) {
            binding.recyclerViewEmpty.visibility = if (it == 0) View.VISIBLE else View.GONE
        }
    }
}