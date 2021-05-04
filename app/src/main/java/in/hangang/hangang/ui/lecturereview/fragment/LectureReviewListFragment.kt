package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.toast.shortToast
import `in`.hangang.core.view.button.checkbox.FilledCheckBox
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentListReviewLectureBinding
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureReviewAdapter
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.util.LogUtil
import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LectureReviewListFragment : ViewBindingFragment<FragmentListReviewLectureBinding>() {
    override val layoutId: Int = R.layout.fragment_list_review_lecture
    private val lectureReviewListViewModel: LectureReviewListViewModel by viewModel()
    private lateinit var lectureReviewAdapter: LectureReviewAdapter
    private val fullMajors: Array<String> by lazy { resources.getStringArray(R.array.major_full) }
    private var majorIdx = 0
    private val checkboxButtonId = arrayOf(
        R.id.review_checkbox_button_major_0,
        R.id.review_checkbox_button_major_1,
        R.id.review_checkbox_button_major_2,
        R.id.review_checkbox_button_major_3,
        R.id.review_checkbox_button_major_4,
        R.id.review_checkbox_button_major_5,
        R.id.review_checkbox_button_major_6,
        R.id.review_checkbox_button_major_7,
        R.id.review_checkbox_button_major_8,
        R.id.review_checkbox_button_major_9
    )
    var reviewcheckboxButtons = arrayOfNulls<FilledCheckBox>(10)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initEvent()
        initViewModel()

    }



    private fun init() {
        for (id in 0..9) {
            reviewcheckboxButtons[id] = binding.root.findViewById(checkboxButtonId[id])
        }
        if (arguments == null) {
            getLectureReviewList()
        } else {
            majorIdx = arguments?.getInt("major")!!
            LogUtil.e(fullMajors[majorIdx])
            lectureReviewListViewModel.selectedMajorList.add(fullMajors[majorIdx])
            reviewcheckboxButtons[majorIdx]?.isChecked = true
            getLectureReviewList()
            reviewcheckboxButtons[majorIdx]?.let {
                lifecycleScope.launch {
                    focusOnViewAsync(binding.lectureReviewHorizontalScrollview, it)
                }
            }

        }
    }


    private fun initEvent() {
        for (id in 0..9) {
            reviewcheckboxButtons[id]?.setOnClickListener {
                if ((it as FilledCheckBox).isChecked) {
                    if (lectureReviewListViewModel.isAddMajorPossible()) {
                        lectureReviewListViewModel.selectedMajorList.add(fullMajors[id])
                        getLectureReviewList()
                    } else {
                        it.isChecked = false
                        context?.shortToast { "학부 정보는 최대 2개까지만 선택 가능합니다" }
                    }
                } else {
                    lectureReviewListViewModel.selectedMajorList.remove(fullMajors[id])
                    getLectureReviewList()
                }
            }
        }


    }

    private fun CoroutineScope.focusOnViewAsync(
        scroll: HorizontalScrollView,
        view: FilledCheckBox
    ) = async(Dispatchers.Main) {
        scroll.smoothScrollTo(view.left, 0)
    }

    private fun initViewModel() {
        with(lectureReviewListViewModel) {
            rankingLectureList.observe(viewLifecycleOwner, {
                it?.let {
                    lectureReviewAdapter = LectureReviewAdapter(scrapLectureList)
                    binding.lectureReviewRecyclerview.adapter = lectureReviewAdapter
                    lectureReviewAdapter.submitData(lifecycle, it) }
            })
        }
    }

    private fun getLectureReviewList() {
        for (i in lectureReviewListViewModel.selectedMajorList) {
            LogUtil.e(i)
        }
        if (lectureReviewListViewModel.selectedMajorList.size == 0)
            lectureReviewListViewModel.getLectureReviewList(lectureReviewListViewModel.selectedMajorListDefault)
        else
            lectureReviewListViewModel.getLectureReviewList(lectureReviewListViewModel.selectedMajorList)
    }


}