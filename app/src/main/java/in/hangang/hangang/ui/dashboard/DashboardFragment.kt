package `in`.hangang.hangang.ui.dashboard

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentDashboardBinding
import `in`.hangang.hangang.util.LogUtil
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardFragment : ViewBindingFragment<FragmentDashboardBinding>() {
    override val layoutId: Int = R.layout.fragment_dashboard
    private val dashBoardViewModelBase: DashBoardViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.textDashboard.setOnClickListener {
            dashBoardViewModelBase.getUsers(true)
        }
        handleObserver()
    }

    private fun handleObserver() {
        dashBoardViewModelBase.getUsers()
        dashBoardViewModelBase.isLoading.observe(viewLifecycleOwner, { isLoading ->
            when (isLoading) {
                true -> showProgressDialog()
                false -> hideProgressDialog()
            }
        })
    }


}