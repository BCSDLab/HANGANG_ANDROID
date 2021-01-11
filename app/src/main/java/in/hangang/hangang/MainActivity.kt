package `in`.hangang.hangang

import `in`.hangang.core.ActivityBase
import `in`.hangang.hangang.ui.home.LectureReviewFragment
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)

        showSimpleDialog(
            message = "OMG",
            positiveButtonText = "확인",
            negativeButtonText = "닫기",
            positiveButtonOnClickListener = {
                dismissSimpleDialog()
            },
            cancelable = false
        )
    }

}