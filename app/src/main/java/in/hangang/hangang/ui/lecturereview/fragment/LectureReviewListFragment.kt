package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.sharedpreference.LectureSearchSharedPreference
import `in`.hangang.core.toast.shortToast
import `in`.hangang.core.util.toEditable
import `in`.hangang.core.view.appbar.SearchAppBar
import `in`.hangang.core.view.button.checkbox.FilledCheckBox
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.LOGIN
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
    private var isComplete = false      //viewModel이 가지고 있는 필터값을 초기화할지 여부를 판단하는 변수
    private lateinit var inputMethodManager: InputMethodManager
    private val queryClickListener: RecyclerViewClickListener = object : RecyclerViewClickListener{     //검색창 검색 리스너
        override fun onClick(view: View, position: Int, item: Any) {
            lectureReviewListViewModel.keyword = item as String //검색 쿼리를 viewModel에 저장
            getLectureReviewList()                              //강의 검색
            binding.lectureReviewRecyclerview.requestFocus()    //포커스를 다른곳에 할당
            binding.lectureReviewSearchbar.searchField.text = lectureReviewListViewModel.keyword!!.toEditable() //검색창에 쿼리를 표현
        }
    }

    private val deleteQueryClickListener: RecyclerViewClickListener = object : RecyclerViewClickListener{       //최근검색어 삭제 리스너
        override fun onClick(view: View, position: Int, item: Any) {
            lectureReviewListViewModel.searchList = LinkedList(LectureSearchSharedPreference.querys) //sharedPreference로부터 최근 검색어리스트를 가져와 ViewModel에 할당
            lectureReviewListViewModel.searchList.removeAt(position)                                 //선택한 검색어 삭제
            LectureSearchSharedPreference.querys = ArrayList(lectureReviewListViewModel.searchList)  //sharePreference에 변경된 최근 검색어 리스트를 저장
            lectureSearchAdapter.submitList(LectureSearchSharedPreference.querys)                    //recyclerview adapter에 최근 검색어 리스트 적용
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
        LogUtil.e("onViewCreated")


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

        initSelecteMajor()  // [메인화면-학부별 탐색]으로 들어올 경우와 [필터화면을 갔다온 경우], [바텀네비게이션-강의평]으로 들어올 경우 분기처리
    }

    /**
     * 선택한 학부 버튼을 체크상태로 변경하는 함
     */
    private fun setMajorButton(){
        if(lectureReviewListViewModel.selectedMajorList.isEmpty()) {
            for(i in 0..9){
                reviewcheckboxButtons[i]?.isChecked = false
            }
        } else {
            for (i in 0..9) {
                reviewcheckboxButtons[i]?.isChecked =
                    lectureReviewListViewModel.selectedMajorList.contains(fullMajors[i])
            }
        }
    }
    private fun initSelecteMajor(){
        /* [바텀네비게이션-강의평]으로 들어온 경우 */
        if (arguments == null && lectureReviewListViewModel.selectedMajorList.size == 0) {
            lectureReviewListViewModel.selectedMajorList.clear()
            setMajorButton()
            getLectureReviewList()
        } else if (arguments == null && lectureReviewListViewModel.selectedMajorList.size >= 0){ // [필터 화면을 갔다가 돌아온 경우]
            setMajorButton()        //선택된 학부버튼 파란색으로 변경
            getLectureReviewList()
        } else {                                                                                    //[메인-학부별탐]으로 들어온 경우
            majorIdx = arguments?.getInt("major")!!
            if(!reviewcheckboxButtons[majorIdx]?.isChecked!!) {
                lectureReviewListViewModel.selectedMajorList.add(fullMajors[majorIdx])
            }
            setMajorButton()        //선택된 학부버튼 파란색으로 변경
            getLectureReviewList()
            /*선택된 학부 버튼으로 스크*/
            reviewcheckboxButtons[majorIdx]?.let {
                lifecycleScope.launch {
                    focusOnViewAsync(binding.lectureReviewHorizontalScrollview, it)
                }
            }

        }
    }


    private fun initEvent() {
        /* 학부버튼 리스너 */
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
        /* 필터버튼 리스너 */
        binding.buttonReviewLectureFilter.setOnClickListener {
                navController.navigate(R.id.action_navigation_evaluation_to_lecture_review_filter)
            isComplete = true
        }
        /* searchbar 백버튼 리스너 */
        binding.lectureReviewSearchbar.onBackButtonClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                binding.lectureReviewSearchbar.showBackButton = false
                binding.lectureReviewSearchbar.searchField.clearFocus()
                hideKeyboard()

            }
        }
        /* searchbar editText 리스너 */
        binding.lectureReviewSearchbar.searchField.setOnClickListener {
            binding.lectureReviewRecyclerview.requestFocus()
            binding.lectureReviewSearchbar.searchField.requestFocus()
        }
        /* searchbar 포커스변경 리스너 */
        binding.lectureReviewSearchbar.searchField.onFocusChangeListener = object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                binding.lectureReviewSearchbar.showBackButton = hasFocus
                if(hasFocus) {
                    changeSearchbarFocusUi(true)
                    var querys = LectureSearchSharedPreference.querys
                    lectureSearchAdapter.submitList(querys)

                } else {
                    changeSearchbarFocusUi(false)
                }
            }
        }
        /* searchbar 서치 리스너 */
        binding.lectureReviewSearchbar.searchListener = object : SearchAppBar.SearchListener {
            override fun onSearch(keyword: String) {
                lectureReviewListViewModel.searchList = LinkedList(LectureSearchSharedPreference.querys)
                if(!keyword.equals("")) {       //검색어를 입력해야만 검색 가능
                    for(i in 0 until lectureReviewListViewModel.searchList.size){   //최근검색어 리스트에 검색어가 있는경우 최근검색어 리스트에서 삭제
                        if(lectureReviewListViewModel.searchList[i].equals(keyword)){
                            lectureReviewListViewModel.searchList.removeAt(i)
                            break
                        }
                    }
                    lectureReviewListViewModel.searchList.addFirst(keyword)     //최근 검색한 keyword를 검색어리스트의 가장 앞에 배치

                    LectureSearchSharedPreference.querys = ArrayList(lectureReviewListViewModel.searchList)//sharedpreference에 최근검색어 리스트 저장
                    lectureReviewListViewModel.keyword = keyword
                    getLectureReviewList()
                    changeSearchbarFocusUi(false)
                    hideKeyboard()
                }
            }
        }


    }
    /* 최근검색어 layout을 노출여부를 설 */
    private fun changeSearchbarFocusUi(isVisible: Boolean){
        if(isVisible) {
            binding.recentlySearchlistConstraintLayout.visibility = View.VISIBLE
            binding.lectureReviewTopConstraintlayout.visibility = View.INVISIBLE
        } else {
            binding.recentlySearchlistConstraintLayout.visibility = View.GONE
            binding.lectureReviewTopConstraintlayout.visibility = View.VISIBLE
        }

    }
    private fun hideKeyboard(){
        inputMethodManager.hideSoftInputFromWindow(
            binding.lectureReviewSearchbar.searchField.windowToken,
            0
        )
    }
    /* 스크롤 함수 */
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


    private fun getLectureReviewList() {
        if (lectureReviewListViewModel.selectedMajorList.size == 0) {       //선택한 학부가 없는 경우
            lectureReviewListViewModel.getLectureReviewList(lectureReviewListViewModel.selectedMajorListDefault)
            lectureReviewListViewModel.getLectureReviewCount(lectureReviewListViewModel.selectedMajorListDefault)
        } else {                                                            //선택한 학부가 있는 경우
            lectureReviewListViewModel.getLectureReviewList(lectureReviewListViewModel.selectedMajorList)
            lectureReviewListViewModel.getLectureReviewCount(lectureReviewListViewModel.selectedMajorListDefault)
        }
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


}