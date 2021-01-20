package `in`.hangang.hangang.ui.dashboard

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentDashboardBinding
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardFragment : ViewBindingFragment<FragmentDashboardBinding>() {
    override val layoutId: Int = R.layout.fragment_dashboard
    private val dashBoardViewModel: DashBoardViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = dashBoardViewModel
        initEvent()
        handleObserver()
    }

    private fun initEvent() {
        binding.nickNameCheckButton.setOnClickListener {
            dashBoardViewModel.checkNickName(binding.nickNameCheckEditText.text.toString())
        }

        binding.emailCheckButton.setOnClickListener {
            dashBoardViewModel.sendEmail(binding.emailCheckEditText.text.toString())
        }
    }

    private fun handleObserver() {
        dashBoardViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            when (isLoading) {
                true -> showProgressDialog()
                false -> hideProgressDialog()
            }
        })
    }
}