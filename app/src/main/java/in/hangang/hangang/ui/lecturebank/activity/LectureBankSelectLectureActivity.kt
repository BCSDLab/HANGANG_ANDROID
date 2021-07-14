package `in`.hangang.hangang.ui.lecturebank.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.view.appbar.SearchAppBar
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityLectureBankSelectLectureBinding
import `in`.hangang.hangang.ui.lecturebank.adapter.LectureBankLectureListAdapter
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankEditorSelectLectureActivityContract
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankSelectLectureViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureBankSelectLectureActivity : ViewBindingActivity<ActivityLectureBankSelectLectureBinding>() {
    override val layoutId = R.layout.activity_lecture_bank_select_lecture

    private val lectureBankSelectLectureViewModel : LectureBankSelectLectureViewModel by viewModel()

    private val lectureBankLectureListAdapter : LectureBankLectureListAdapter by lazy {
        LectureBankLectureListAdapter().apply {
            setOnItemClickListener { position, lecture, isSelected ->
                if(isSelected) {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                else {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(LectureBankEditorSelectLectureActivityContract.OUTPUT_LECTURE, lecture)
                    })
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()

        lectureBankSelectLectureViewModel.getLectureBankLectureList()
    }

    private fun initView() {
        with(binding) {
            recyclerViewLectureList.apply {
                layoutManager = LinearLayoutManager(this@LectureBankSelectLectureActivity)
                adapter = lectureBankLectureListAdapter
                lectureBankLectureListAdapter.selectedLecture = intent.extras?.getParcelable(LectureBankEditorSelectLectureActivityContract.INPUT_LECTURE)
            }

            appBar.setSearchListener {
                lectureBankSelectLectureViewModel.setKeyword(it.ifBlank { null })
            }

            appBar.onBackButtonClickListener = View.OnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }

            majorsView.setOnSelectionChangedListener { _, simpleMajorString ->
                lectureBankSelectLectureViewModel.setDepartment(simpleMajorString)
            }
        }
    }

    private fun initViewModel() {
        with(lectureBankSelectLectureViewModel) {
            lectureBankLectureList.observe(this@LectureBankSelectLectureActivity) {
                lectureBankLectureListAdapter.submitData(lifecycle, it)
            }
        }
    }
}