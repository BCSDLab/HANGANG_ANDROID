package `in`.hangang.hangang.ui.home.recommendedlectures.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.evaluation.LectureDoc
import `in`.hangang.hangang.databinding.FragmentHomeRecommendedLecturesBinding
import `in`.hangang.hangang.ui.home.recommendedlectures.adapter.RecommendedLecturesRecyclerViewAdapter
import `in`.hangang.hangang.ui.home.recommendedlectures.viewmodel.RecommendedLecturesFragmentViewModel
import `in`.hangang.hangang.ui.lecturereview.adapter.RecommendedDocsAdapter
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecommendedLecturesFragment : ViewBindingFragment<FragmentHomeRecommendedLecturesBinding>() {
    override val layoutId = R.layout.fragment_home_recommended_lectures

    private val recommendedLecturesFragmentViewModel: RecommendedLecturesFragmentViewModel by viewModel()
    private val recommendedDocsAdapter = RecommendedDocsAdapter()
    private val navController: NavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private val lectureDocsItemClickListener = object : RecyclerViewClickListener{
        override fun onClick(view: View, position: Int, item: Any) {
            val bundle = Bundle()
            bundle.putInt("LECTURE_DOC_ID", (item as LectureDoc).id)
            navController.navigate(R.id.navigation_lecture_bank, bundle)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        subscribe()
    }
    fun init(){
        binding.recyclerViewRecommendedLectures.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewRecommendedLectures.adapter = recommendedDocsAdapter
        recommendedDocsAdapter.setLectureDocClickListener(lectureDocsItemClickListener)
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