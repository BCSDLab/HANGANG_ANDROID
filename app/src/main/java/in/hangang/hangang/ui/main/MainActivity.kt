package `in`.hangang.hangang.ui.main

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.base.viewmodel.ViewModelBase
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityMainBinding
import `in`.hangang.hangang.ui.timetable.viewmodel.*
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main

    private val loadingMap = mutableMapOf<ViewModelBase, Boolean>()
    private val isLoading = MutableLiveData<Map<ViewModelBase, Boolean>>()

    private val timetableViewModel: TimetableViewModel by viewModel()
    private val timetableFragmentViewModel: TimetableFragmentViewModel by viewModel()
    private val timetableLectureListViewModel: TimetableLectureListViewModel by viewModel()
    private val timetableCustomLectureViewModel: TimetableCustomLectureViewModel by viewModel()
    private val timetableLectureDetailViewModel: TimetableLectureDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLoading()

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.navView, navController)
        binding.navView.setOnNavigationItemReselectedListener {}
    }

    private fun initLoading() {
        isLoading.observe(this) {
            if(it.values.contains(true)) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        /*timetableViewModel.isLoading.observe(this) {
            loadingMap[timetableViewModel] = it
            isLoading.value = loadingMap
        }*/
        timetableFragmentViewModel.isLoading.observe(this) {
            loadingMap[timetableFragmentViewModel] = it
            isLoading.value = loadingMap
        }
        /*timetableLectureListViewModel.isLoading.observe(this) {
            loadingMap[timetableLectureListViewModel] = it
            isLoading.value = loadingMap
        }
        timetableCustomLectureViewModel.isLoading.observe(this) {
            loadingMap[timetableCustomLectureViewModel] = it
            isLoading.value = loadingMap
        }
        timetableLectureDetailViewModel.isLoading.observe(this) {
            loadingMap[timetableLectureDetailViewModel] = it
            isLoading.value = loadingMap
        }*/
    }
}