package `in`.hangang.hangang.di

import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordActivityViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordFragmentViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationFragmentViewModel
import `in`.hangang.hangang.ui.dashboard.DashBoardViewModel
import `in`.hangang.hangang.ui.home.mytimetable.viewmodel.MyTimetableFragmentViewModel
import `in`.hangang.hangang.ui.home.recentlectures.viewmodel.RecentLecturesFragmentViewModel
import `in`.hangang.hangang.ui.home.recommendedlectures.viewmodel.RecommendedLecturesFragmentViewModel
import `in`.hangang.hangang.ui.login.LoginViewModel
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpEmailViewModel
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpMajorViewModel
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableListActivityViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableFragmentViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashBoardViewModel(get()) }
    viewModel { SignUpEmailViewModel(get()) }
    viewModel { (handle: String) -> SignUpViewModel(get(), handle) }
    viewModel { (portalAccount: String, nickName: String, password: String) -> SignUpMajorViewModel(get(), portalAccount, nickName, password) }
    //Change password activity, fragments
    viewModel { ChangePasswordActivityViewModel() }
    viewModel { ChangePasswordFragmentViewModel(get()) }
    viewModel { EmailAuthenticationFragmentViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    //Home
    viewModel { MyTimetableFragmentViewModel() }
    viewModel { RecommendedLecturesFragmentViewModel() }
    viewModel { RecentLecturesFragmentViewModel() }
    //Timetable
    viewModel { TimetableViewModel(get()) }
    viewModel { TimetableFragmentViewModel() }
    viewModel { TimetableListActivityViewModel() }
    viewModel { TimetableLectureViewModel(get()) }
}