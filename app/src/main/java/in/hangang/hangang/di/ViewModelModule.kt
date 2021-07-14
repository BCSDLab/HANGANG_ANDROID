package `in`.hangang.hangang.di

import `in`.hangang.hangang.data.source.repository.LectureRepository
import `in`.hangang.hangang.data.source.repository.TimeTableRepository
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordActivityViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.ChangePasswordFragmentViewModel
import `in`.hangang.hangang.ui.changepassword.viewmodel.EmailAuthenticationFragmentViewModel
import `in`.hangang.hangang.ui.home.mytimetable.viewmodel.MyTimetableFragmentViewModel
import `in`.hangang.hangang.ui.home.ranking.viewmodel.RankingLectureViewModel
import `in`.hangang.hangang.ui.home.recentlectures.viewmodel.RecentLecturesFragmentViewModel
import `in`.hangang.hangang.ui.home.recommendedlectures.viewmodel.RecommendedLecturesFragmentViewModel
import `in`.hangang.hangang.ui.lecturebank.viewmodel.*
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureEvaluationViewModel
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewDetailViewModel
import `in`.hangang.hangang.ui.mypage.viewmodel.MyPageViewModel
import `in`.hangang.hangang.ui.mypage.viewmodel.MyScrapLectureBankViewModel
import `in`.hangang.hangang.ui.mypage.viewmodel.MyScrapLectureReviewViewModel
import `in`.hangang.hangang.ui.settings.viewmodel.MyProfileActivityViewModel
import `in`.hangang.hangang.ui.settings.viewmodel.SettingsActivityViewModel
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.ui.login.LoginViewModel
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpEmailViewModel
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpMajorViewModel
import `in`.hangang.hangang.ui.signup.viewmodel.SignUpViewModel
import `in`.hangang.hangang.ui.splash.SplashViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableAddActivityViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureDetailViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableLectureListViewModel
import `in`.hangang.hangang.ui.timetable.viewmodel.TimetableViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LectureReviewListViewModel(get()) }
    viewModel { SignUpEmailViewModel(get()) }
    viewModel { (handle: String) -> SignUpViewModel(get(), handle) }
    viewModel { (portalAccount: String, nickName: String, password: String) ->
        SignUpMajorViewModel(
            get(),
            portalAccount,
            nickName,
            password
        )
    }
    //Change password activity, fragments
    viewModel { ChangePasswordActivityViewModel() }
    viewModel { ChangePasswordFragmentViewModel(get()) }
    viewModel { EmailAuthenticationFragmentViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    //Home
    viewModel { MyTimetableFragmentViewModel(get(), get()) }
    viewModel { RecommendedLecturesFragmentViewModel(get()) }
    viewModel { RecentLecturesFragmentViewModel(get()) }
    viewModel { RankingLectureViewModel(get()) }
    viewModel { LectureReviewDetailViewModel(get() as LectureRepository, get() as TimeTableRepository) }
    viewModel { LectureEvaluationViewModel(get()) }

    //Timetable
    viewModel { TimetableViewModel(get()) }
    viewModel { TimetableLectureListViewModel(get(), get()) }
    viewModel { TimetableAddActivityViewModel(get()) }
    viewModel { TimetableLectureDetailViewModel(get(), get()) }
    //Mypage
    viewModel { MyPageViewModel(get()) }
    viewModel { MyScrapLectureReviewViewModel(get()) }
    viewModel { MyScrapLectureBankViewModel(get()) }

    viewModel { SplashViewModel(get()) }
    viewModel { SettingsActivityViewModel(get()) }
    viewModel { MyProfileActivityViewModel(get()) }

    //LectureBank
    viewModel { LectureBankViewModel(get()) }
    viewModel { LectureBankDetailViewModel(get(), get()) }
    viewModel { LectureBankUploadFileViewModel(get()) }
    viewModel { LectureBankEditorViewModel(get()) }
    viewModel { LectureBankSelectLectureViewModel(get()) }
}
