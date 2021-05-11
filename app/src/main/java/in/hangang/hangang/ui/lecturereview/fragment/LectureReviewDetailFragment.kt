package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.FragmentLectureReviewDetailBinding
import `in`.hangang.hangang.util.LogUtil
import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation

class LectureReviewDetailFragment : ViewBindingFragment<FragmentLectureReviewDetailBinding>() {
    override val layoutId = R.layout.fragment_lecture_review_detail
    private val navController: NavController by lazy {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lecture = arguments?.getParcelable<RankingLectureItem>("lecture")
        binding.lecture = lecture
    }
}
