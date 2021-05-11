package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.toast.shortToast
import `in`.hangang.core.view.button.checkbox.FilledCheckBox
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentListReviewLectureBinding
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureReviewAdapter
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.util.LogUtil
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.HorizontalScrollView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LectureReviewListFragment : ViewBindingFragment<FragmentListReviewLectureBinding>() {
    override val layoutId: Int = R.layout.fragment_list_review_lecture
    private val lectureReviewListViewModel: LectureReviewListViewModel by sharedViewModel()
    private lateinit var lectureReviewAdapter: LectureReviewAdapter
    private val fullMajors: Array<String> by lazy { resources.getStringArray(R.array.major_full) }
    private var majorIdx = 0
    private var isComplete = false
    private lateinit var inputMethodManager: InputMethodManager
    private val navController: NavController by lazy {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
    }
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
        initViewModel()
        init()
        initEvent()


    }



    private fun init() {
        inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        for (id in 0..9) {
            reviewcheckboxButtons[id] = binding.root.findViewById(checkboxButtonId[id])
        }
        lectureReviewListViewModel.getScrapList()

    }
    private fun initSelecteMajor(){
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
        binding.buttonReviewLectureFilter.setOnClickListener {
            navController.navigate(R.id.action_navigation_evaluation_to_lecture_review_filter)
        }
        binding.lectureReviewSearchbar.onBackButtonClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                binding.lectureReviewSearchbar.showBackButton = false
                binding.lectureReviewSearchbar.searchField.clearFocus()
                hideKeyboard()

            }
        }


    }
    private fun hideKeyboard(){
        inputMethodManager.hideSoftInputFromWindow(
            binding.lectureReviewSearchbar.searchField.windowToken,
            0
        )
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
                    if(isComplete)
                        lectureReviewAdapter.submitData(lifecycle, it)
                }
            })
            isGetScrapList.observe(viewLifecycleOwner,{
                if(it){
                    lectureReviewAdapter = LectureReviewAdapter(scrapLectureList)
                    binding.lectureReviewRecyclerview.adapter = lectureReviewAdapter
                    isComplete = true
                    initSelecteMajor()
                }
            })
        }

    }

    override fun onStop() {
        super.onStop()
        isComplete = false
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