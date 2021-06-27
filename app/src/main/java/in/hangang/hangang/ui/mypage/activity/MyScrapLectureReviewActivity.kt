package `in`.hangang.hangang.ui.mypage.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.core.view.dp2Px
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityMyScrapLectureReviewBinding
import `in`.hangang.hangang.ui.mypage.adapter.MyScrapLectureReviewAdapter
import `in`.hangang.hangang.ui.mypage.viewmodel.MyScrapViewModel
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyScrapLectureReviewActivity : ViewBindingActivity<ActivityMyScrapLectureReviewBinding>() {

    override val layoutId = R.layout.activity_my_scrap_lecture_review

    private val myScrapViewModel: MyScrapViewModel by viewModel()

    private val myScrapLectureReviewAdapter: MyScrapLectureReviewAdapter by lazy {
        MyScrapLectureReviewAdapter().apply {
            setOnItemClickListener { parent, view, position ->
                if (myScrapViewModel.isEditMode.value == true) {
                    toggleLectureSelection(position)
                    myScrapViewModel.changeRemoveButtonState(isLeastOneSelected())
                } else {
                    //TODO 강의평 상세페이지 이동
                }
            }
        }
    }
    private val editButton: View by lazy {
        appBarTextButton(
            getString(R.string.edit)
        )
    }
    private val finishButton: View by lazy {
        appBarTextButton(
            getString(R.string.my_scrap_edit_finish)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()

        myScrapViewModel.getMyScrapLecture()
        myScrapViewModel.setEditMode(false)
    }

    private fun initViewModel() {
        with(myScrapViewModel) {
            isLoading.observe(this@MyScrapLectureReviewActivity) {
                if (it) showProgressDialog()
                else hideProgressDialog()
            }
            myScrapLecture.observe(this@MyScrapLectureReviewActivity) {
                myScrapLectureReviewAdapter.setLectures(it)
            }
            canRemoveLecture.observe(this@MyScrapLectureReviewActivity) {
                binding.buttonRemoveSelectedLecture.isEnabled = it
            }
            isEditMode.observe(this@MyScrapLectureReviewActivity) {
                myScrapLectureReviewAdapter.isEditMode = it
                if (it) {
                    editButton.visibility = View.GONE
                    finishButton.visibility = View.VISIBLE
                    binding.buttonRemoveSelectedLecture.visibility = View.VISIBLE
                    binding.buttonSelectAll.visibility = View.VISIBLE
                    with(binding.recyclerViewMyScrap) {
                        setPadding(
                            this.paddingLeft,
                            this.paddingTop,
                            this.paddingLeft,
                            dp2Px(RECYCLER_VIEW_PADDING_IS_EDIT_DP)
                        )
                    }
                } else {
                    editButton.visibility = View.VISIBLE
                    finishButton.visibility = View.GONE
                    binding.buttonRemoveSelectedLecture.visibility = View.GONE
                    binding.buttonSelectAll.visibility = View.GONE
                    with(binding.recyclerViewMyScrap) {
                        setPadding(
                            this.paddingLeft,
                            this.paddingTop,
                            this.paddingLeft,
                            dp2Px(RECYCLER_VIEW_PADDING_IS_NOT_EDIT_DP)
                        )
                    }
                }
            }
        }
    }

    private fun initView() {
        with(binding.appBar) {
            addViewInRight(editButton)
            addViewInRight(finishButton)
            setOnAppBarButtonClickListener({ _, _ -> }, { view, _ ->
                when (view) {
                    editButton -> myScrapViewModel.setEditMode(true)
                    finishButton -> myScrapViewModel.setEditMode(false)
                }
            })
        }
        with(binding.recyclerViewMyScrap) {
            layoutManager = LinearLayoutManager(this@MyScrapLectureReviewActivity)
            adapter = myScrapLectureReviewAdapter
        }
        binding.buttonSelectAll.setOnClickListener {
            with(myScrapLectureReviewAdapter) {
                if (isSelectedAll()) unselectAllLecture()
                else selectAllLecture()
                myScrapViewModel.changeRemoveButtonState(myScrapLectureReviewAdapter.isLeastOneSelected())
            }
        }
        binding.buttonRemoveSelectedLecture.setOnClickListener {
            myScrapViewModel.unscrapLecture(*myScrapLectureReviewAdapter.getSelectedLectures().toTypedArray())
        }
    }

    companion object {
        private const val RECYCLER_VIEW_PADDING_IS_NOT_EDIT_DP = 8f
        private const val RECYCLER_VIEW_PADDING_IS_EDIT_DP = 88f
    }
}
