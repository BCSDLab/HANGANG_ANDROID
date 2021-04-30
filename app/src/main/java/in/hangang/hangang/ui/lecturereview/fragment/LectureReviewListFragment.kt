package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.button.radiobutton.FilledRadioButton
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentListReviewLectureBinding
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureReviewAdapter
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.util.LogUtil
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.RadioButton
import androidx.core.view.children
import androidx.paging.PagingData
import org.koin.androidx.viewmodel.ext.android.viewModel


class LectureReviewListFragment : ViewBindingFragment<FragmentListReviewLectureBinding>() {
    override val layoutId: Int = R.layout.fragment_list_review_lecture
    private val lectureReviewListViewModel: LectureReviewListViewModel by viewModel()
    private val adapter = LectureReviewAdapter()
    private val fullMajors: Array<String> by lazy { resources.getStringArray(R.array.major_full) }
    private var majorIdx = 0
    private val radioButtonId = arrayOf(R.id.review_radio_button_major_0, R.id.review_radio_button_major_1, R.id.review_radio_button_major_2, R.id.review_radio_button_major_3, R.id.review_radio_button_major_4,
        R.id.review_radio_button_major_5, R.id.review_radio_button_major_6, R.id.review_radio_button_major_7, R.id.review_radio_button_major_8, R.id.review_radio_button_major_9)
    var reviewRadioButtons = arrayOfNulls<FilledRadioButton>(10)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lectureReviewRecyclerview.adapter = adapter
        initViewModel()
        init()
        initEvent()



    }
    private fun init(){
        for(id in 0..9){
            reviewRadioButtons[id] = binding.root.findViewById(radioButtonId[id])
        }
        if (arguments == null) {
            getLectureReviewList("")
        } else {
            majorIdx = arguments?.getInt("major")!!
            reviewRadioButtons[majorIdx]?.isChecked = true
            getLectureReviewList(fullMajors[majorIdx])
            reviewRadioButtons[majorIdx]?.let {
                focusOnView(binding.lectureReviewHorizontalScrollview, it
                )
            }

        }
    }
    private fun initEvent(){
        binding.reivewRadioGroupDepartment.setOnCheckedChangeListener { group, checkedId ->
            LogUtil.e(checkedId.toString())
            majorIdx = getSeletedIdx(checkedId)
            adapter.submitData(lifecycle, PagingData.empty())
            getLectureReviewList(fullMajors[majorIdx])
        }
    }

    private fun getSeletedIdx(id: Int): Int {
        when (id) {
            R.id.review_radio_button_major_0 -> return 0
            R.id.review_radio_button_major_1 -> return 1
            R.id.review_radio_button_major_2 -> return 2
            R.id.review_radio_button_major_3 -> return 3
            R.id.review_radio_button_major_4 -> return 4
            R.id.review_radio_button_major_5 -> return 5
            R.id.review_radio_button_major_6 -> return 6
            R.id.review_radio_button_major_7 -> return 7
            R.id.review_radio_button_major_8 -> return 8
            R.id.review_radio_button_major_9 -> return 9
            else -> return -1
        }
    }

    private fun focusOnView(scroll: HorizontalScrollView, view: FilledRadioButton) {
        Handler().post(Runnable {
            scroll.smoothScrollTo(view.left, 0)
        })
    }

    private fun initViewModel() {
        with(lectureReviewListViewModel) {
            rankingLectureList.observe(viewLifecycleOwner, {
                it?.let { adapter.submitData(lifecycle, it) }
            })
        }
    }

    private fun getLectureReviewList(major: String) {
        lectureReviewListViewModel.getLectureReviewList(major)
    }


}