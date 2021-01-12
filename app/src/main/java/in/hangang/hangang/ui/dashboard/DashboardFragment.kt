package `in`.hangang.hangang.ui.dashboard

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentDashboardBinding
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels

class DashboardFragment :
    ViewBindingFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    private val dashBoardViewModelBase: DashBoardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleObserver()
        binding.textDashboard.setOnClickListener {
            dashBoardViewModelBase.getUsers(true)
        }
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