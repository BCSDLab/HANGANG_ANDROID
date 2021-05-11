package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.toast.shortToast
import `in`.hangang.core.view.button.checkbox.FilledCheckBox
import `in`.hangang.core.view.button.radiobutton.FilledRadioButton
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.SORT_BY_LATEST_REVIEW
import `in`.hangang.hangang.constant.SORT_BY_REVIEW_COUNT
import `in`.hangang.hangang.constant.SORT_BY_TOTAL_RATING
import `in`.hangang.hangang.databinding.FragmentLectureReviewFilterBinding
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.util.LogUtil
import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.compose.runtime.key
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class LectureReviewFilterFragment : ViewBindingFragment<FragmentLectureReviewFilterBinding>() {
    override val layoutId = R.layout.fragment_lecture_review_filter
    private val filterTypeId = arrayOf(
        R.id.type_filter_1,
        R.id.type_filter_2,
        R.id.type_filter_3,
        R.id.type_filter_4,
        R.id.type_filter_5,
        R.id.type_filter_6,
        R.id.type_filter_7,
        R.id.type_filter_8
    )
    private val filterHashTagId = arrayOf(
        R.id.hash_tag_type_1,
        R.id.hash_tag_type_2,
        R.id.hash_tag_type_3,
        R.id.hash_tag_type_4,
        R.id.hash_tag_type_5,
        R.id.hash_tag_type_6,
        R.id.hash_tag_type_7,
        R.id.hash_tag_type_8,
        R.id.hash_tag_type_9
    )
    private var filterSort = ""
    private var filterType = ArrayList<String>()
    private var filterHashTag = ArrayList<Int>()
    private var keyword: String? = null
    var filterTypeIsChecked = booleanArrayOf(false,false,false,false,false,false,false,false)
    var filterHashTagIsChecked =
        booleanArrayOf(false, false, false, false, false, false, false, false, false)
    private val filterTypeCheckBox = arrayOfNulls<FilledCheckBox>(8)
    private val filterHashTagCheckBox = arrayOfNulls<FilledCheckBox>(9)
    private val navController: NavController by lazy {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
    }
    private val lectureReviewListViewModel: LectureReviewListViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentData = this
        init()
        initEvent()
    }
    private fun init(){
        for(id in 0..7){
            filterTypeCheckBox[id] = binding.root.findViewById(filterTypeId[id])
        }
        for(id in 0..8){
            filterHashTagCheckBox[id] = binding.root.findViewById(filterHashTagId[id])
        }
        binding.lectureReviewRadiogroup.children.iterator().forEach { view ->
            (view as FilledRadioButton).isChecked = (view as FilledRadioButton).text.toString() == lectureReviewListViewModel.filterSort
        }
        filterType = lectureReviewListViewModel.filterType.clone() as ArrayList<String>
        filterSort = lectureReviewListViewModel.filterSort
        filterHashTag = lectureReviewListViewModel.filterHashTag.clone() as ArrayList<Int>
        filterTypeIsChecked = lectureReviewListViewModel.filterTypeIsChecked.clone()
        filterHashTagIsChecked = lectureReviewListViewModel.filterHashTagIsChecked.clone()
    }
    private fun initEvent(){
        binding.lectureReviewCloseButton.setOnClickListener {
            navController.navigate(R.id.action_lecture_review_filter_to_navigation_evaluation)
        }
        binding.lectureReviewRadiogroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.sort_by_total_rating -> {
                    filterSort = SORT_BY_TOTAL_RATING
                }
                R.id.sort_by_review_count -> {
                    filterSort = SORT_BY_REVIEW_COUNT
                }
                else -> {
                    filterSort = SORT_BY_LATEST_REVIEW
                }
            }
        }
        for(i in 0..7){
            filterTypeCheckBox[i]?.setOnClickListener {
                if((it as FilledCheckBox).isChecked){
                    filterType.add(it.text.toString())
                    filterTypeIsChecked[i] = true
                }else{
                    filterType.remove(it.text.toString())
                    filterTypeIsChecked[i] = false
                }
            }
        }
        for(i in 0..8){
            filterHashTagCheckBox[i]?.setOnClickListener {
                if((it as FilledCheckBox).isChecked){
                    filterHashTag.add(i)
                    filterHashTagIsChecked[i] = true
                }else{
                    filterHashTag.remove(i)
                    filterHashTagIsChecked[i] = false
                }
            }
        }
        binding.lectureReviewFilterApplyButton.setOnClickListener {
            LogUtil.e("applly")
            setFilters()
            context?.shortToast { "필터가 적용되었습니다." }
        }
        binding.lectureReviewFilterButton.setOnClickListener {
            setFiltersClear()
            binding.sortByTotalRating.isChecked = true
        }


    }
    private fun setFiltersClear(){
        filterSort = SORT_BY_TOTAL_RATING
        filterType.clear()
        filterHashTag.clear()
        for(i in 0..7){
            filterTypeCheckBox[i]?.isChecked = false
        }
        for(i in 0..8){
            filterHashTagCheckBox[i]?.isChecked = false
        }
        Arrays.fill(filterTypeIsChecked, false)
        Arrays.fill(filterHashTagIsChecked, false)
    }
    private fun setFilters(){
        lectureReviewListViewModel.filterSort = filterSort
        lectureReviewListViewModel.filterType = filterType
        lectureReviewListViewModel.filterHashTag = filterHashTag
        lectureReviewListViewModel.filterTypeIsChecked = filterTypeIsChecked
        lectureReviewListViewModel.filterHashTagIsChecked = filterHashTagIsChecked
    }
}