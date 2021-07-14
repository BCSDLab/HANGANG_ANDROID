package `in`.hangang.hangang.ui.mypage.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentMyPageBinding
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankUploadFileViewModel
import `in`.hangang.hangang.ui.mypage.contract.MyPagePointRecordActivityContract
import `in`.hangang.hangang.ui.mypage.contract.MyPagePurchasedBankActivityContract
import `in`.hangang.hangang.ui.mypage.contract.MyScrapLectureBankActivityContract
import `in`.hangang.hangang.ui.mypage.contract.MyScrapLectureReviewActivityContract
import `in`.hangang.hangang.ui.mypage.viewmodel.MyPageViewModel
import `in`.hangang.hangang.ui.settings.activity.SettingsActivity
import `in`.hangang.hangang.ui.settings.contract.SettingsActivityContract
import `in`.hangang.hangang.util.file.FileUtil
import `in`.hangang.hangang.util.withThread
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxjava3.kotlin.addTo
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyPageFragment : ViewBindingFragment<FragmentMyPageBinding>() {
    override val layoutId = R.layout.fragment_my_page

    private val fileUtil : FileUtil by inject()

    private val myPageViewModel: MyPageViewModel by sharedViewModel()
    private val lectureBankUploadFileViewModel : LectureBankUploadFileViewModel by sharedViewModel()
    private val lectureBankFileAdapter: LectureBankFileAdapter by lazy {
        LectureBankFileAdapter().apply {
            isEnabled = true
            setOnItemClickListener { _, uploadFile, b ->
                if(!b) {
                    lectureBankUploadFileViewModel.downloadOrOpenFile(requireContext().applicationContext, uploadFile)
                }
            }
        }
    }

    private val myPagePointRecordActivityContract = registerForActivityResult(MyPagePointRecordActivityContract()) {}
    private val myPagePurchasedBankActivityContract =
        registerForActivityResult(MyPagePurchasedBankActivityContract()) {}
    private val myScrapLectureReviewActivityContract = registerForActivityResult(MyScrapLectureReviewActivityContract()) {}
    private val myScrapLectureBankActivityContract = registerForActivityResult(MyScrapLectureBankActivityContract()) {}
    private val settingsActivityContract = registerForActivityResult(SettingsActivityContract()) {}

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
                if (it) showProgressDialog()
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
        with(lectureBankUploadFileViewModel) {
            downloadUrlEvent.observe(viewLifecycleOwner, EventObserver {
                fileUtil.downloadFile(it.downloadUrl, it.uploadFile)
                    .withThread()
                    .subscribe({ downloadStatus ->
                        lectureBankFileAdapter.setDownloadStatus(
                            downloadStatus.uploadFile,
                            downloadStatus.downloadedBytes,
                            when (downloadStatus.status) {
                                DownloadManager.STATUS_PENDING -> -1
                                DownloadManager.STATUS_RUNNING -> downloadStatus.totalBytes
                                else -> -2
                            }
                        )
                    }, {

                    }, {
                        lectureBankFileAdapter.setDownloadStatus(it.uploadFile, 0, -2)
                    })
                    .addTo(compositeDisposable)
            })
            openFileEvent.observe(viewLifecycleOwner, EventObserver {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, it))
                } catch (activityNotFoundException: ActivityNotFoundException) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.upload_file_activity_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun initView() {
        with(binding) {
            imageButtonGotoSettings.setOnClickListener {
                settingsActivityContract.launch(null)
            }
            textViewPurchasedBank.setOnClickListener {
                myPagePurchasedBankActivityContract.launch(null)
            }
            textViewMyScrapLectureReview.setOnClickListener {
                myScrapLectureReviewActivityContract.launch(null)
            }
            textViewMyScrapLectureBank.setOnClickListener {
                myScrapLectureBankActivityContract.launch(null)
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