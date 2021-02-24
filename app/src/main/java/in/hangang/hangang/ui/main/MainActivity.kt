package `in`.hangang.hangang.ui.main

import `in`.hangang.core.base.activity.ActivityBase
import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityMainBinding
import `in`.hangang.hangang.util.startActivityForResult
import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.navView, navController)
        binding.navView.setOnNavigationItemReselectedListener {}

        startActivityForResult(
            activityResultContract = ActivityResultContracts.RequestMultiplePermissions(),
            input = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_CALENDAR)
        ).subscribe { it, _ ->
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}