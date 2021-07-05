package `in`.hangang.hangang.ui.home.findbymajor.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentHomeFindByMajorBinding
import `in`.hangang.hangang.ui.home.findbymajor.adapter.FindByMajorRecyclerViewAdapter
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

class FindByMajorFragment : ViewBindingFragment<FragmentHomeFindByMajorBinding>() {
    override val layoutId = R.layout.fragment_home_find_by_major

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFindByMajor.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFindByMajor.adapter = FindByMajorRecyclerViewAdapter(requireActivity())
    }
}