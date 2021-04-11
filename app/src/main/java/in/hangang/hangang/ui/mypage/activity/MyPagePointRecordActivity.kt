package `in`.hangang.hangang.ui.mypage.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.setVisibility
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityMyPagePointBinding
import `in`.hangang.hangang.ui.mypage.adapter.MyPagePointRecordAdapter
import `in`.hangang.hangang.ui.mypage.viewmodel.MyPageViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPagePointRecordActivity : ViewBindingActivity<ActivityMyPagePointBinding>() {
    override val layoutId = R.layout.activity_my_page_point

    private val myPageViewModel: MyPageViewModel by viewModel()
    private val myPagePointRecordAdapter : MyPagePointRecordAdapter by lazy {
        MyPagePointRecordAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
        myPageViewModel.getPointRecord()
    }

    private fun initViewModel() {
        with(myPageViewModel) {
            isLoading.observe(this@MyPagePointRecordActivity) {
                binding.progressPointRecord.setVisibility(it)
                binding.recyclerViewPointRecord.setVisibility(!it)
            }
            pointRecords.observe(this@MyPagePointRecordActivity) {
                myPagePointRecordAdapter.setPointRecords(it)
            }
        }

    }

    private fun initView() {
        with(binding) {
            textViewPoint.text = "${(intent.extras?.getInt("point"))}P"
            recyclerViewPointRecord.apply {
                layoutManager = LinearLayoutManager(this@MyPagePointRecordActivity)
                adapter = myPagePointRecordAdapter
            }
        }
    }
}