package `in`.hangang.hangang.ui.mypage.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityMyPagePointBinding
import `in`.hangang.hangang.ui.mypage.viewmodel.MyPageViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPagePointRecordActivity : ViewBindingActivity<ActivityMyPagePointBinding>() {
    override val layoutId = R.layout.activity_my_page_point

    private val myPageViewModel: MyPageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        TODO("Not yet implemented")
    }

    private fun initView() {
        with(binding) {
            textViewPoint.text = "${(intent.extras.getInt("point")).toString()}P"

        }
    }
}