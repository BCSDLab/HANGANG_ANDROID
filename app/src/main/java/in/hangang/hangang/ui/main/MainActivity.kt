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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.navView, navController)
        binding.navView.setOnNavigationItemReselectedListener {}
    }
}