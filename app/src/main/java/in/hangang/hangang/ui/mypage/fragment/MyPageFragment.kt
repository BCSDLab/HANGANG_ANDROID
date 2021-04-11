package `in`.hangang.hangang.ui.mypage.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentMyPageBinding
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import `in`.hangang.hangang.ui.mypage.contract.MyPagePointRecordActivityContract
import `in`.hangang.hangang.ui.mypage.viewmodel.MyPageViewModel
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyPageFragment : ViewBindingFragment<FragmentMyPageBinding>() {
    override val layoutId = R.layout.fragment_my_page

    private val myPageViewModel: MyPageViewModel by sharedViewModel()
    private val lectureBankFileAdapter : LectureBankFileAdapter by lazy {
        LectureBankFileAdapter(requireContext())
    }
    private val myPagePointRecordActivityContract = registerForActivityResult(MyPagePointRecordActivityContract()) {}

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
            viewMyPoint.setOnClickListener {
                myPagePointRecordActivityContract.launch(myPageViewModel.user.value?.point ?: -1)
            }
            recyclerViewBoughtBank.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = lectureBankFileAdapter
            }
        }
    }
}