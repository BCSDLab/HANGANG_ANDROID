package `in`.hangang.hangang.di

import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordActivityViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordFragmentViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationFragmentViewModel
import `in`.hangang.hangang.ui.dashboard.DashBoardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashBoardViewModel(get()) }

    //Change password activity, fragments
    viewModel { ChangePasswordActivityViewModel() }
    viewModel { ChangePasswordFragmentViewModel(get()) }
    viewModel { EmailAuthenticationFragmentViewModel(get()) }
}