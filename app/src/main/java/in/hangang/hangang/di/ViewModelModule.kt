package `in`.hangang.hangang.di

import `in`.hangang.hangang.data.source.LectureRepository
import `in`.hangang.hangang.data.source.TimeTableRepository
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordActivityViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordFragmentViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationFragmentViewModel
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.ui.login.LoginViewModel
import `in`.hangang.hangang.ui.home.mytimetable.viewmodel.MyTimetableFragmentViewModel
import `in`.hangang.hangang.ui.home.ranking.viewmodel.RankingLectureViewModel
import `in`.hangang.hangang.ui.home.recentlectures.viewmodel.RecentLecturesFragmentViewModel
import `in`.hangang.hangang.ui.home.recommendedlectures.viewmodel.RecommendedLecturesFragmentViewModel
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewDetailViewModel
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpEmailViewModel
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpMajorViewModel
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LectureReviewListViewModel(get()) }
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
    viewModel { RankingLectureViewModel(get()) }
    viewModel { LectureReviewDetailViewModel(get() as LectureRepository, get() as TimeTableRepository) }

}