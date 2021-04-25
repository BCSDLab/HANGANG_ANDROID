package `in`.hangang.hangang.ui.mypage.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityMyPagePurchasedBankBinding
import `in`.hangang.hangang.databinding.FragmentMyPageBinding
import `in`.hangang.hangang.ui.mypage.adapter.MyPagePurchasedBankAdapter
import `in`.hangang.hangang.ui.mypage.viewmodel.MyPageViewModel
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPagePurchasedBankActivity : ViewBindingActivity<ActivityMyPagePurchasedBankBinding>() {
    override val layoutId = R.layout.activity_my_page_purchased_bank

    private val myPageViewModel: MyPageViewModel by viewModel()
    private val myPagePurchasedBankAdapter : MyPagePurchasedBankAdapter by lazy {
        MyPagePurchasedBankAdapter().apply {
            setOnItemClickListener { recyclerView: RecyclerView?, view: View?, i: Int ->
                //TODO 강의평가 화면 이동
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()

        myPageViewModel.getPurchasedBanks()
    }

    private fun initViewModel() {
        with(myPageViewModel) {
            isLoading.observe(this@MyPagePurchasedBankActivity) {
                if(it) showProgressDialog()
                else hideProgressDialog()
            }
            purchasedBanks.observe(this@MyPagePurchasedBankActivity) {
                myPagePurchasedBankAdapter.setLectureBanks(it)
            }
        }
    }

    private fun initView() {
        with(binding.recyclerViewFileInPurchasedBank) {
            layoutManager = LinearLayoutManager(this@MyPagePurchasedBankActivity)
            adapter = myPagePurchasedBankAdapter
        }
    }
}