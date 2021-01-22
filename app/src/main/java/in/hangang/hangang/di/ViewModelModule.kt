package `in`.hangang.hangang.di

import `in`.hangang.hangang.ui.dashboard.DashBoardViewModel
import `in`.hangang.hangang.ui.signup.SignUpEmailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashBoardViewModel(get()) }
    viewModel { SignUpEmailViewModel(get()) }
}