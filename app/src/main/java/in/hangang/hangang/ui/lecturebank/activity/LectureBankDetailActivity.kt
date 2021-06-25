package `in`.hangang.hangang.ui.lecturebank.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.util.hideKeyboard
import `in`.hangang.core.util.showKeyboard
import `in`.hangang.core.util.toProperCapacityUnit
import `in`.hangang.core.view.showPopupMenu
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankComment
import `in`.hangang.hangang.data.entity.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.data.entity.uploadfile.UploadFile
import `in`.hangang.hangang.databinding.ActivityLectureBankDetailBinding
import `in`.hangang.hangang.ui.LectureBankFileAdapter
import `in`.hangang.hangang.ui.lecturebank.adapter.LectureBankCommentsAdapter
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankDetailActivityContract
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankDetailViewModel
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankUploadFileViewModel
import `in`.hangang.hangang.util.file.FileUtil
import `in`.hangang.hangang.util.withThread
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.kotlin.addTo
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureBankDetailActivity : ViewBindingActivity<ActivityLectureBankDetailBinding>() {
    override val layoutId = R.layout.activity_lecture_bank_detail

    private val lectureBankDetailViewModel: LectureBankDetailViewModel by viewModel()
    private val lectureBankUploadFileViewModel: LectureBankUploadFileViewModel by viewModel()
    private val fileUtil: FileUtil by inject()
    private var isShowingCommentField = false

    private val lectureBankCommentsAdapter: LectureBankCommentsAdapter by lazy {
        LectureBankCommentsAdapter()
    }

    private val lectureBankFileAdapter: LectureBankFileAdapter by lazy {
        LectureBankFileAdapter()
    }

    private var lastClickedUploadFile: UploadFile? = null
    private var lastClickedUploadFilePosition: Int? = null
    private var lastClickedUploadFileDownloadUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()

        val id = intent.extras?.getInt(LectureBankDetailActivityContract.LECTURE_BANK_ID) ?: -1
        if (id > 0) {
            lectureBankDetailViewModel.getLectureBankDetail(id)
        } else {
            finish()
        }
    }

    private fun initView() {
        hideCommentField()
        with(binding) {
            buttonLectureBankMore.setOnClickListener {
                it.showPopupMenu(R.menu.menu_lecture_bank_detail).apply {
                    val isScraped = lectureBankDetailViewModel.isScraped.value ?: false
                    this.menu.findItem(R.id.menu_lecture_bank_scrap).isVisible = !isScraped
                    this.menu.findItem(R.id.menu_lecture_bank_unscrap).isVisible = isScraped
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menu_lecture_bank_scrap -> {
                                lectureBankDetailViewModel.scrapLecture(
                                    lectureBankDetailViewModel.lectureBankDetail.value?.id ?: -1
                                )
                                true
                            }
                            R.id.menu_lecture_bank_unscrap -> {
                                lectureBankDetailViewModel.unscrapLecture()
                                true
                            }
                            R.id.menu_lecture_bank_report -> {
                                showLectureBankReportDialog()
                                true
                            }
                            else -> false
                        }
                    }
                }
            }
            editTextComment.filters = arrayOf(InputFilter.LengthFilter(COMMENT_MAX_TEXT_COUNT))
            editTextComment.addTextChangedListener {
                textViewCommentTextCount.text =
                    getString(R.string.lecture_bank_comment_text_count, it?.length ?: 0, COMMENT_MAX_TEXT_COUNT)
                imageViewCommentApply.isEnabled = !it.isNullOrBlank()
            }
            imageViewCommentApply.setOnClickListener {
                lectureBankDetailViewModel.commentLectureBank(editTextComment.text.toString())
                editTextComment.setText("")
                hideCommentField()
            }
            recyclerViewCommentList.layoutManager = LinearLayoutManager(this@LectureBankDetailActivity)
            recyclerViewCommentList.adapter = lectureBankCommentsAdapter
            recyclerViewAttachFileList.layoutManager =
                LinearLayoutManager(this@LectureBankDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewAttachFileList.adapter = lectureBankFileAdapter
            lectureBankCommentsAdapter.setOnItemClickListener { view, lectureBankComment, position ->
                view.showPopupMenu(
                    if (lectureBankComment.userId == lectureBankDetailViewModel.userId) R.menu.menu_lecture_bank_my_comment
                    else R.menu.menu_lecture_bank_comment
                ).setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.item_menu_lecture_bank_comment_report -> {
                            showLectureBankCommentReportDialog(commentId = lectureBankComment.id ?: 0)
                            true
                        }
                        R.id.item_menu_lecture_bank_comment_edit -> {
                            lectureBankCommentsAdapter.lectureBankCommentEditPosition = position
                            true
                        }
                        R.id.item_menu_lecture_bank_comment_delete -> {
                            showLectureBankDeleteCommentDialog(lectureBankComment)
                            true
                        }
                        else -> false
                    }
                }
            }
            lectureBankCommentsAdapter.setOnModifyButtonClickListener { _, lectureBankComment, s ->
                lectureBankDetailViewModel.modifyLectureBankComment(lectureBankComment, s)
            }
            lectureBankFileAdapter.setOnItemClickListener { i, uploadFile, b ->
                if (!b) {
                    lectureBankUploadFileViewModel.downloadOrOpenFile(this@LectureBankDetailActivity, uploadFile)
                }
            }
            textViewComment.setOnClickListener {
                showCommentField()
            }
            layout.setOnClickListener {
                hideCommentField()
            }
            buttonPurchase.setOnClickListener {
                showLectureBankPurchaseDialog()
            }
            layoutThumbsCount.setOnClickListener {
                lectureBankDetailViewModel.toggleHitLectureBank()
            }
        }
    }

    private fun initViewModel() {
        with(lectureBankDetailViewModel) {
            isLoading.observe(this@LectureBankDetailActivity) {
                if (it) showProgressDialog()
                else hideProgressDialog()
            }
            lectureBankDetail.observe(this@LectureBankDetailActivity) {
                setLectureBankDetail(it)
                lectureBankDetailViewModel.getLectureBankComments()
            }
            lectureBankCommentPagingData.observe(this@LectureBankDetailActivity) {
                lectureBankCommentsAdapter.submitData(lifecycle, it)
            }
            reportedEvent.observe(this@LectureBankDetailActivity, EventObserver {
                showReportedDialog()
            })
            errorEvent.observe(this@LectureBankDetailActivity, EventObserver {
                showErrorDialog(it)
            })
            isPurchased.observe(this@LectureBankDetailActivity) {
                lectureBankFileAdapter.isEnabled = it.first != LectureBankDetailViewModel.PurchaseStatus.NOT_PURCHASED
                binding.buttonPurchase.apply {
                    text =
                        when(it.first) {
                            LectureBankDetailViewModel.PurchaseStatus.NOT_PURCHASED ->
                                getString(
                                    R.string.lecture_bank_detail_purchase_with_point,
                                    it.second.toString()
                                )
                            LectureBankDetailViewModel.PurchaseStatus.PURCHASED ->
                                getString(R.string.lecture_bank_purchased)
                            LectureBankDetailViewModel.PurchaseStatus.MY_LECTURE_BANK ->
                                getString(R.string.lecture_bank_purchased_my_lecture_bank)
                        }
                    isEnabled = it.first == LectureBankDetailViewModel.PurchaseStatus.NOT_PURCHASED
                }
            }
            lectureBankCommentAppliedEvent.observe(this@LectureBankDetailActivity) {
                lectureBankDetailViewModel.getLectureBankComments()
            }
            lectureBankCommentModifiedEvent.observe(this@LectureBankDetailActivity) {
                lectureBankCommentsAdapter.lectureBankCommentEditPosition = -1
                lectureBankDetailViewModel.getLectureBankComments()
            }
            lectureBankCommentRemovedEvent.observe(this@LectureBankDetailActivity) {
                lectureBankCommentsAdapter.lectureBankCommentEditPosition = -1
                lectureBankDetailViewModel.getLectureBankComments()
            }
            hitChanged.observe(this@LectureBankDetailActivity) {
                with(binding.textViewThumbsUpCount) {
                    text = (this.text.toString().toInt() + (if(this.isSelected) -1 else 1)).toString()
                    isSelected = !this.isSelected
                }
                binding.imageViewThumbsUp.isSelected = !binding.imageViewThumbsUp.isSelected
            }
        }
        with(lectureBankUploadFileViewModel) {
            downloadUrlEvent.observe(this@LectureBankDetailActivity, EventObserver {
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
            openFileEvent.observe(this@LectureBankDetailActivity, EventObserver {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, it))
                } catch (activityNotFoundException: ActivityNotFoundException) {
                    Toast.makeText(
                        this@LectureBankDetailActivity,
                        getString(R.string.upload_file_activity_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    override fun onBackPressed() {
        if (isShowingCommentField) {
            hideCommentField()
        } else
            super.onBackPressed()
    }

    private fun setLectureBankDetail(lectureBankDetail: LectureBankDetail) {
        with(binding) {
            bank = lectureBankDetail
            textViewAttachFileInfo.text = getString(
                R.string.lecture_bank_detail_attachment_info,
                lectureBankDetail.uploadFiles.sumOf { it.size }.toProperCapacityUnit(2)
            )
            textViewLectureBankCreatedDate.text =
                getString(R.string.lecture_bank_created_date, lectureBankDetail.createdAt.split("T")[0])
            imageViewThumbsUp.isSelected = lectureBankDetail.isHit
            textViewThumbsUpCount.isSelected = lectureBankDetail.isHit
            lectureBankFileAdapter.setFiles(lectureBankDetail.uploadFiles)
        }

        Glide.with(this).load(lectureBankDetail.thumbnail).into(binding.imageViewLectureBankImage)
    }

    private fun showLectureBankReportDialog() {
        AlertDialog.Builder(this)
            .setItems(R.array.lecture_bank_report_types) { dialog, which ->
                lectureBankDetailViewModel.reportLecture(which + 1)
            }
            .show()
    }

    private fun showLectureBankCommentReportDialog(commentId: Int) {
        AlertDialog.Builder(this)
            .setItems(R.array.lecture_bank_report_types) { dialog, which ->
                lectureBankDetailViewModel.reportComment(commentId, which + 1)
            }
            .show()
    }

    private fun showLectureBankPurchaseDialog() {
        showSimpleDialog(
            message = getString(R.string.lecture_bank_purchase_message),
            positiveButtonText = getString(R.string.lecture_bank_proceed_purchase),
            positiveButtonOnClickListener = { dialog, _ ->
                lectureBankDetailViewModel.purchaseLectureBank()
                dialog.dismiss()
            },
            negativeButtonText = getString(R.string.cancel),
            negativeButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            }
        )
    }

    private fun showLectureBankDeleteCommentDialog(lectureBankComment: LectureBankComment) {
        showSimpleDialog(
            message = getString(R.string.lecture_bank_delete_comment_message),
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dialog, _ ->
                lectureBankDetailViewModel.removeLectureBankComment(lectureBankComment)
                dialog.dismiss()
            },
            negativeButtonText = getString(R.string.cancel),
            negativeButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            }
        )
    }

    private fun showReportedDialog() {
        showSimpleDialog(
            title = getString(R.string.lecture_bank_reported_title),
            message = getString(R.string.lecture_bank_reported_message),
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            }
        )
    }

    private fun showErrorDialog(error: CommonResponse) {
        showSimpleDialog(
            message = error.errorMessage ?: "",
            positiveButtonText = getString(R.string.ok),
            positiveButtonOnClickListener = { dialog, _ ->
                dialog.dismiss()
            }
        )
    }

    private fun hideCommentField() {
        binding.fieldComment.visibility = View.GONE
        isShowingCommentField = false
        hideKeyboard()
    }

    private fun showCommentField() {
        binding.fieldComment.visibility = View.VISIBLE
        isShowingCommentField = true
        binding.editTextComment.requestFocus()
        showKeyboard(binding.editTextComment)
    }

    companion object {
        const val COMMENT_MAX_TEXT_COUNT = 300
    }
}