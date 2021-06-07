package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.sharedpreference.LectureSearchSharedPreference
import `in`.hangang.core.toast.shortToast
import `in`.hangang.core.view.appbar.SearchAppBar
import `in`.hangang.core.view.button.checkbox.FilledCheckBox
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentListReviewLectureBinding
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureReviewAdapter
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureSearchAdapter
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.util.LogUtil
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.HorizontalScrollView
import android.widget.ImageView
import androidx.compose.runtime.key
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.collections.ArrayList


class LectureReviewListFragment : ViewBindingFragment<FragmentListReviewLectureBinding>() {
    override val layoutId: Int = R.layout.fragment_list_review_lecture
    private val lectureReviewListViewModel: LectureReviewListViewModel by sharedViewModel()
    private lateinit var lectureReviewAdapter: LectureReviewAdapter
    private var lectureSearchAdapter = LectureSearchAdapter()
    private val fullMajors: Array<String> by lazy { resources.getStringArray(R.array.major_full) }
    private var majorIdx = 0
    private var isComplete = false
    private lateinit var inputMethodManager: InputMethodManager
    private val queryClickListener: RecyclerViewClickListener = object : RecyclerViewClickListener{
        override fun onClick(view: View, position: Int, item: Any) {
            lectureReviewListViewModel.keyword = item as String
            getLectureReviewList()
            binding.lectureReviewRecyclerview.requestFocus()
        }
    }
    private val deleteQueryClickListener: RecyclerViewClickListener = object : RecyclerViewClickListener{
        override fun onClick(view: View, position: Int, item: Any) {
            LogUtil.e("click3")
            lectureReviewListViewModel.searchList = LectureSearchSharedPreference.querys
            lectureReviewListViewModel.searchList.removeAt(position)
            LectureSearchSharedPreference.querys = lectureReviewListViewModel.searchList
            lectureSearchAdapter.submitList(LectureSearchSharedPreference.querys)
        }
    }
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
        init()
        initViewModel()

        initEvent()


    }

    override fun onStart() {
        super.onStart()
        LogUtil.e("onstart")
    }

    override fun onResume() {
        super.onResume()
        LogUtil.e("onresume")
    }

    override fun onPause() {
        super.onPause()
        LogUtil.e("onpause")
    }




    private fun init() {
        inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        lectureReviewAdapter = LectureReviewAdapter(requireActivity())
        binding.lectureReviewRecyclerview.adapter = lectureReviewAdapter
        binding.recentlyLectureReviewSearchRecyclerview.adapter = lectureSearchAdapter
        lectureSearchAdapter.setClickQueryRecyclerViewListener(queryClickListener)
        lectureSearchAdapter.setDeleteRecyclerViewListener(deleteQueryClickListener)
        for (id in 0..9) {
            reviewcheckboxButtons[id] = binding.root.findViewById(checkboxButtonId[id])
        }

        initSelecteMajor()
        //lectureReviewListViewModel.getScrapList()

    }
    private fun setMajorButton(){
        for(i in 0..9){
            reviewcheckboxButtons[i]?.isChecked = lectureReviewListViewModel.selectedMajorList.contains(fullMajors[i])
        }
    }
    private fun initSelecteMajor(){
        if (arguments == null && lectureReviewListViewModel.selectedMajorList.size == 0) {
            lectureReviewListViewModel.selectedMajorList.clear()
            getLectureReviewList()
        } else if(arguments == null && lectureReviewListViewModel.selectedMajorList.size >= 0){
            setMajorButton()
            getLectureReviewList()
        }
        else {
            majorIdx = arguments?.getInt("major")!!
            if(!reviewcheckboxButtons[majorIdx]?.isChecked!!) {
                lectureReviewListViewModel.selectedMajorList.add(fullMajors[majorIdx])
            }
            setMajorButton()
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
            isComplete = true
        }
        binding.lectureReviewSearchbar.onBackButtonClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                binding.lectureReviewSearchbar.showBackButton = false
                binding.lectureReviewSearchbar.searchField.clearFocus()
                hideKeyboard()

            }
        }
        binding.lectureReviewSearchbar.searchField.setOnClickListener {
            LogUtil.e("focus")
            binding.lectureReviewRecyclerview.requestFocus()
            binding.lectureReviewSearchbar.searchField.requestFocus()
        }
        binding.lectureReviewSearchbar.searchField.onFocusChangeListener = object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                binding.lectureReviewSearchbar.showBackButton = hasFocus

                if(hasFocus) {
                    LogUtil.e(hasFocus.toString())
                    binding.recentlySearchlistConstraintLayout.visibility = View.VISIBLE
                    binding.lectureReviewTopConstraintlayout.visibility = View.INVISIBLE
                    //lectureSearchAdapter.currentList.clear()
                    var a = LectureSearchSharedPreference.querys
                    LogUtil.e(a.size.toString())
                    lectureSearchAdapter.submitList(a)

                }
                else {
                    LogUtil.e(hasFocus.toString())
                    binding.recentlySearchlistConstraintLayout.visibility = View.GONE
                    binding.lectureReviewTopConstraintlayout.visibility = View.VISIBLE
                }
            }
        }
        binding.lectureReviewSearchbar.searchListener = object : SearchAppBar.SearchListener {
            override fun onSearch(keyword: String) {
                LogUtil.e("click2")
                lectureReviewListViewModel.searchList = LectureSearchSharedPreference.querys
                if(!keyword.equals("")) {
                    for(i in 0 until lectureReviewListViewModel.searchList.size){
                        if(lectureReviewListViewModel.searchList[i].equals(keyword)){
                            lectureReviewListViewModel.searchList.removeAt(i)
                        }
                    }
                    lectureReviewListViewModel.searchList.add(keyword)

                    Collections.reverse(lectureReviewListViewModel.searchList)

                    LectureSearchSharedPreference.querys = lectureReviewListViewModel.searchList
                    //lectureSearchAdapter.currentList.clear()
                    lectureReviewListViewModel.keyword = keyword
                    getLectureReviewList()
                    binding.recentlySearchlistConstraintLayout.visibility = View.GONE
                    binding.lectureReviewTopConstraintlayout.visibility = View.VISIBLE
                    hideKeyboard()
                }




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
                it?.let { lectureReviewAdapter.submitData(lifecycle, it) }

            })
            lectureLsitItemCount.observe(viewLifecycleOwner, {
                if(it == 0){
                    binding.lectureReviewEmptyResultLinearlayout.visibility = View.VISIBLE
                }else{
                    binding.lectureReviewEmptyResultLinearlayout.visibility = View.GONE
                }
            })
        }

    }

    override fun onStop() {
        super.onStop()
        if(!isComplete)
            lectureReviewListViewModel.clear()
        LogUtil.e("onstop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.e("ondestroyview")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        LogUtil.e("onDetach()")
    }


    private fun getLectureReviewList() {
        for (i in lectureReviewListViewModel.selectedMajorList) {
            LogUtil.e(i)
        }
        if (lectureReviewListViewModel.selectedMajorList.size == 0) {
            lectureReviewListViewModel.getLectureReviewList(lectureReviewListViewModel.selectedMajorListDefault)
            lectureReviewListViewModel.getLectureReviewCount(lectureReviewListViewModel.selectedMajorListDefault)
        } else {
            lectureReviewListViewModel.getLectureReviewList(lectureReviewListViewModel.selectedMajorList)
            lectureReviewListViewModel.getLectureReviewCount(lectureReviewListViewModel.selectedMajorListDefault)
        }
    }


}