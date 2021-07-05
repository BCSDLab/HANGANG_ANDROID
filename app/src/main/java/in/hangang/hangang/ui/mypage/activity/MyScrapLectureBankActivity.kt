package `in`.hangang.hangang.ui.mypage.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.core.view.dp2Px
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.lecturebank.toLectureBank
import `in`.hangang.hangang.databinding.ActivityMyScrapLectureBankBinding
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankDetailActivityContract
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankEditorActivityContract
import `in`.hangang.hangang.ui.mypage.adapter.MyScrapLectureBankAdapter
import `in`.hangang.hangang.ui.mypage.viewmodel.MyScrapLectureBankViewModel
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyScrapLectureBankActivity : ViewBindingActivity<ActivityMyScrapLectureBankBinding>() {
    override val layoutId = R.layout.activity_my_scrap_lecture_bank

    private val myScrapLectureBankViewModel: MyScrapLectureBankViewModel by viewModel()

    private val lectureBankDetailActivityContract = registerForActivityResult(LectureBankDetailActivityContract()) {}

    private val myScrapLectureBankAdapter: MyScrapLectureBankAdapter by lazy {
        MyScrapLectureBankAdapter().apply {
            setOnItemClickListener { position, lectureBankScrap ->
                if (myScrapLectureBankViewModel.isEditMode.value == true) {
                    toggleLectureSelection(position)
                    myScrapLectureBankViewModel.changeRemoveButtonState(isLeastOneSelected())
                } else {
                    lectureBankDetailActivityContract.launch(lectureBankScrap.id)
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

        myScrapLectureBankViewModel.getLectureBankScraps()
        myScrapLectureBankViewModel.setEditMode(false)
    }

    private fun initViewModel() {
        with(myScrapLectureBankViewModel) {
            isLoading.observe(this@MyScrapLectureBankActivity) {
                if (it) showProgressDialog()
                else hideProgressDialog()
            }
            scraps.observe(this@MyScrapLectureBankActivity) {
                binding.recyclerViewMyScrap.isVisible = it.isNotEmpty()
                binding.layoutMyScrapEmpty.isVisible = it.isEmpty()
                myScrapLectureBankAdapter.setLectureBanks(it)
            }
            canRemoveLectureBank.observe(this@MyScrapLectureBankActivity) {
                binding.buttonRemoveSelectedLecture.isEnabled = it
            }
            isEditMode.observe(this@MyScrapLectureBankActivity) {
                myScrapLectureBankAdapter.isEditMode = it
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
                    editButton -> myScrapLectureBankViewModel.setEditMode(true)
                    finishButton -> myScrapLectureBankViewModel.setEditMode(false)
                }
            })
        }
        with(binding.recyclerViewMyScrap) {
            layoutManager = LinearLayoutManager(this@MyScrapLectureBankActivity)
            adapter = myScrapLectureBankAdapter
        }
        binding.buttonSelectAll.setOnClickListener {
            with(myScrapLectureBankAdapter) {
                if (isSelectedAll()) unselectAllLecture()
                else selectAllLecture()
                myScrapLectureBankViewModel.changeRemoveButtonState(myScrapLectureBankAdapter.isLeastOneSelected())
            }
        }
        binding.buttonRemoveSelectedLecture.setOnClickListener {
            myScrapLectureBankViewModel.unscrapLecture(*myScrapLectureBankAdapter.getSelectedLectures().toTypedArray())
        }
    }

    companion object {
        private const val RECYCLER_VIEW_PADDING_IS_NOT_EDIT_DP = 8f
        private const val RECYCLER_VIEW_PADDING_IS_EDIT_DP = 88f
    }
}