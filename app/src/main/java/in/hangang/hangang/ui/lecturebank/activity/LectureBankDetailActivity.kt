package `in`.hangang.hangang.ui.lecturebank.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.base.activity.showSimpleDialog
import `in`.hangang.core.livedata.EventObserver
import `in`.hangang.core.util.toProperCapacityUnit
import `in`.hangang.core.view.showPopupMenu
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.lecturebank.LectureBankDetail
import `in`.hangang.hangang.data.response.CommonResponse
import `in`.hangang.hangang.databinding.ActivityLectureBankDetailBinding
import `in`.hangang.hangang.ui.lecturebank.adapter.LectureBankCommentsAdapter
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankDetailActivityContract
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankDetailViewModel
import android.app.AlertDialog
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureBankDetailActivity : ViewBindingActivity<ActivityLectureBankDetailBinding>() {
    override val layoutId = R.layout.activity_lecture_bank_detail

    private val lectureBankDetailViewModel: LectureBankDetailViewModel by viewModel()

    private val lectureBankCommentsAdapter : LectureBankCommentsAdapter by lazy {
        LectureBankCommentsAdapter()
    }

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
        with(binding) {
            buttonLectureBankMore.setOnClickListener {
                it.showPopupMenu(R.menu.menu_lecture_bank_detail).apply {
                    val isScraped = lectureBankDetailViewModel.isScraped.value ?: false
                    this.menu.findItem(R.id.menu_lecture_bank_scrap).isVisible = !isScraped
                    this.menu.findItem(R.id.menu_lecture_bank_unscrap).isVisible = isScraped
                    setOnMenuItemClickListener { item ->
                        when(item.itemId) {
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
            recyclerViewCommentList.layoutManager = LinearLayoutManager(this@LectureBankDetailActivity)
            recyclerViewCommentList.adapter = lectureBankCommentsAdapter
            lectureBankCommentsAdapter.setOnItemClickListener { //Report button clicked
                showLectureBankCommentReportDialog(commentId = it.id)
            }
        }
    }

    private fun initViewModel() {
        with(lectureBankDetailViewModel) {
            isLoading.observe(this@LectureBankDetailActivity) {
                if(it) showProgressDialog()
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
        }
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
            buttonPurchase.apply {
                text =
                    if (lectureBankDetail.isPurchased) {
                        getString(R.string.lecture_bank_purchased)
                    } else {
                        getString(
                            R.string.lecture_bank_detail_purchase_with_point,
                            lectureBankDetail.pointPrice.toString()
                        )
                    }
                isEnabled = !lectureBankDetail.isPurchased
            }
            imageViewThumbsUp.isSelected = lectureBankDetail.isHit
            textViewThumbsUpCount.isSelected = lectureBankDetail.isHit
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

    companion object {

    }
}