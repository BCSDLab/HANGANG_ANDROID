package `in`.hangang.hangang.ui.splash

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivitySplashBinding
import `in`.hangang.hangang.ui.login.LoginActivity
import `in`.hangang.hangang.ui.main.MainActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : ViewBindingActivity<ActivitySplashBinding>() {
    override val layoutId: Int = R.layout.activity_splash
    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = splashViewModel
        delay()
        initObserver()
    }

    private fun init() {
        splashViewModel.checkAccessTokenValid()
    }

    private fun initObserver() {
        with(splashViewModel) {
            isTokenValid.observe(this@SplashActivity) {
                if (it) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                finish()
            }
        }
    }
    private fun delay(){
        val mHandler = Handler()
        mHandler.postDelayed({
            init()
        }, 1000)
    }
}
