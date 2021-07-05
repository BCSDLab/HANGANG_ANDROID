package `in`.hangang.hangang.ui.mypage.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityMyPagePurchasedBankBinding
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankDetailActivityContract
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankUploadFileViewModel
import `in`.hangang.hangang.ui.mypage.adapter.MyPagePurchasedBankAdapter
import `in`.hangang.hangang.ui.mypage.viewmodel.MyPageViewModel
import `in`.hangang.hangang.util.withThread
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxjava3.kotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPagePurchasedBankActivity : ViewBindingActivity<ActivityMyPagePurchasedBankBinding>() {
    override val layoutId = R.layout.activity_my_page_purchased_bank

    private val myPageViewModel: MyPageViewModel by viewModel()
    private val lectureBankUploadFileViewModel: LectureBankUploadFileViewModel by viewModel()

    private val lectureBankDetailActivityContract = registerForActivityResult(
        LectureBankDetailActivityContract()
    ) {}

    private val myPagePurchasedBankAdapter: MyPagePurchasedBankAdapter by lazy {
        MyPagePurchasedBankAdapter().apply {
            setOnItemClickListener({ _, lectureBank ->
                lectureBankDetailActivityContract.launch(lectureBank.id)
            }, { _, lectureBank, uploadFile, b ->
                lectureBankDetailActivityContract.launch(lectureBank.id)
            })
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
                if (it) showProgressDialog()
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