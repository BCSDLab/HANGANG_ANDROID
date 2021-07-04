package `in`.hangang.hangang.ui.home.recommendedlectures.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentHomeRecommendedLecturesBinding
import `in`.hangang.hangang.ui.home.recommendedlectures.adapter.RecommendedLecturesRecyclerViewAdapter
import `in`.hangang.hangang.ui.home.recommendedlectures.viewmodel.RecommendedLecturesFragmentViewModel
import `in`.hangang.hangang.ui.lecturereview.adapter.RecommendedDocsAdapter
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecommendedLecturesFragment : ViewBindingFragment<FragmentHomeRecommendedLecturesBinding>() {
    override val layoutId = R.layout.fragment_home_recommended_lectures

    private val recommendedLecturesFragmentViewModel: RecommendedLecturesFragmentViewModel by viewModel()
    private val recommendedDocsAdapter = RecommendedDocsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        subscribe()
    }
    fun init(){
        binding.recyclerViewRecommendedLectures.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewRecommendedLectures.adapter = recommendedDocsAdapter

        recommendedLecturesFragmentViewModel.getLectureBankHit()
    }
    fun subscribe() {
        recommendedLecturesFragmentViewModel.recommendedLectureDocs.observe(viewLifecycleOwner, {

            it.let {
                if(it.isEmpty()){
                    binding.recyclerViewEmpty.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewEmpty.visibility = View.GONE
                }
                recommendedDocsAdapter.submitList(it)
            }
        })
    }
}