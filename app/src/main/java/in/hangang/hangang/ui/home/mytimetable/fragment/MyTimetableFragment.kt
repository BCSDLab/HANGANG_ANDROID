package `in`.hangang.hangang.ui.home.mytimetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentHomeMyTimetableBinding
import `in`.hangang.hangang.ui.home.mytimetable.adapter.MyTimetableAdapter
import `in`.hangang.hangang.ui.home.mytimetable.viewmodel.MyTimetableFragmentViewModel
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyTimetableFragment : ViewBindingFragment<FragmentHomeMyTimetableBinding>() {
    override val layoutId = R.layout.fragment_home_my_timetable

    private val myTimetableFragmentViewModel: MyTimetableFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewMyTimetable.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewMyTimetable.adapter = MyTimetableAdapter()

        myTimetableFragmentViewModel.timetableCount.observe(viewLifecycleOwner) {
            binding.recyclerViewEmpty.visibility = if (it == 0) View.VISIBLE else View.GONE
        }
    }
}