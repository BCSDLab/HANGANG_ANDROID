package `in`.hangang.hangang.di

import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationViewModel
import `in`.hangang.hangang.ui.dashboard.DashBoardViewModel
import `in`.hangang.hangang.ui.signup.SignUpEmailViewModel
import `in`.hangang.hangang.ui.signup.SignUpMajorViewModel
import `in`.hangang.hangang.ui.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashBoardViewModel(get()) }
    viewModel { SignUpEmailViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
    viewModel { EmailAuthenticationViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { SignUpMajorViewModel(get()) }
}