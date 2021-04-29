package `in`.hangang.hangang.ui.lecturereview

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentListReviewLectureBinding
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel


class LectureReviewListFragment : ViewBindingFragment<FragmentListReviewLectureBinding>() {
    override val layoutId: Int = R.layout.fragment_list_review_lecture
    private val dashBoardViewModel: LectureReviewListViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}