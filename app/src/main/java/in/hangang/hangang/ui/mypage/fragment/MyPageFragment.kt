package `in`.hangang.hangang.ui.mypage.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentMyPageBinding
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import `in`.hangang.hangang.ui.mypage.contract.MyPagePointRecordActivityContract
import `in`.hangang.hangang.ui.mypage.contract.MyPagePurchasedBankActivityContract
import `in`.hangang.hangang.ui.mypage.contract.MyScrapActivityContract
import `in`.hangang.hangang.ui.mypage.viewmodel.MyPageViewModel
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyPageFragment : ViewBindingFragment<FragmentMyPageBinding>() {
    override val layoutId = R.layout.fragment_my_page

    private val myPageViewModel: MyPageViewModel by sharedViewModel()
    private val lectureBankFileAdapter : LectureBankFileAdapter by lazy {
        LectureBankFileAdapter()
    }

    private val myPagePointRecordActivityContract = registerForActivityResult(MyPagePointRecordActivityContract()) {}
    private val myPagePurchasedBankActivityContract = registerForActivityResult(MyPagePurchasedBankActivityContract()) {}
    private val myScrapActivityContract = registerForActivityResult(MyScrapActivityContract()) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()

        myPageViewModel.getMyPageData()
        myPageViewModel.getUploadFiles()
    }

    private fun initViewModel() {
        with(myPageViewModel) {
            isLoading.observe(viewLifecycleOwner) {
                if(it) showProgressDialog()
                else hideProgressDialog()
            }
            user.observe(viewLifecycleOwner) {
                binding.textViewNickname.text = it.nickname
                binding.textViewMajor.text = it.major.joinToString(separator = ", ", prefix = "", postfix = "")
                binding.textViewPoint.text = "${it.point}P"
            }
            userCount.observe(viewLifecycleOwner) {
                binding.textViewCommentCount.text = it.getLectureBankCommentCount.toString()
                binding.textViewLectureBankCount.text = it.getLectureBankCount.toString()
                binding.textViewLectureReviewCount.text = it.LectureReview.toString()
            }
            uploadFiles.observe(viewLifecycleOwner) {
                lectureBankFileAdapter.setFiles(it)
            }
        }
    }

    private fun initView() {
        with(binding) {
            imageButtonGotoSettings.setOnClickListener {
                //TODO go to settings activity
            }
            textViewPurchasedBank.setOnClickListener {
                myPagePurchasedBankActivityContract.launch(null)
            }
            textViewMyScrap.setOnClickListener {
                myScrapActivityContract.launch(null)
            }
            viewMyPoint.setOnClickListener {
                myPagePointRecordActivityContract.launch(myPageViewModel.user.value?.point ?: -1)
            }
            recyclerViewPurchasedBankFile.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = lectureBankFileAdapter
            }
        }
    }
}