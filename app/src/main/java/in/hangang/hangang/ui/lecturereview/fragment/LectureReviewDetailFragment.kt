package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.RECENTLY_READ_LECTURE_REVIEW
import `in`.hangang.hangang.data.entity.evaluation.ClassLecture
import `in`.hangang.hangang.data.entity.evaluation.LectureDoc
import `in`.hangang.hangang.data.entity.evaluation.LectureReview
import `in`.hangang.hangang.data.entity.ranking.RankingLectureItem
import `in`.hangang.hangang.data.entity.timetable.TimeTable
import `in`.hangang.hangang.data.request.LectureReviewReportRequest
import `in`.hangang.hangang.databinding.FragmentLectureReviewDetailBinding
import `in`.hangang.hangang.ui.lecturereview.activity.LectureEvaluationActivity
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureClassTimeAdapter
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureDetailReviewAdapter
import `in`.hangang.hangang.ui.lecturereview.adapter.ListDialogRecyclerViewAdapter
import `in`.hangang.hangang.ui.lecturereview.adapter.RecommendedDocsAdapter
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewDetailViewModel
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.initScoreChart
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.orhanobut.hawk.Hawk
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ClassCastException
import java.util.*
import kotlin.collections.ArrayList

class LectureReviewDetailFragment : ViewBindingFragment<FragmentLectureReviewDetailBinding>() {
    override val layoutId = R.layout.fragment_lecture_review_detail
    lateinit var lecture: RankingLectureItem
    private lateinit var lectureClassTimeAdapter: LectureClassTimeAdapter
    private val recommendedDocsAdapter = RecommendedDocsAdapter()
    private val lectureDetailReviewAdapter = LectureDetailReviewAdapter()
    private val listDialogRecyclerViewAdapter = ListDialogRecyclerViewAdapter()
    private lateinit var timeTableListDialog: TimeTableListDialog
    private var classLectureId: Int = 0
    private var timeTableId: Int = 0

    private val reportList: Array<String> by lazy { resources.getStringArray(R.array.report_item) }
    private val navController: NavController by lazy {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
    }
    private val lectureDocsItemClickListener = object : RecyclerViewClickListener{
        override fun onClick(view: View, position: Int, item: Any) {
            val bundle = Bundle()
            bundle.putInt("LECTURE_DOC_ID", (item as LectureDoc).id)
            navController.navigate(R.id.navigation_lecture_bank, bundle)
        }
    }

    private val timeTableListDialogClickListener =
        object : TimeTableListDialog.TimeTableListDialogClickListener {
            override fun onConfirmClick(view: DialogFragment) {
                showProgressDialog()
                view.dismiss()
                lectureReviewDetailViewModel.fetchClassLectureList(lecture.id, lectureReviewDetailViewModel.semester.value?.id?.toLong()!!)
            }

            override fun onCancelClick(view: DialogFragment) {
                view.dismiss()
            }
        }

    /* 시간표 담기/뺴기 리스 */
    private val classClickListener = object : RecyclerViewClickListener {
        override fun onClick(view: View, position: Int, item: Any) {
            val classLecture = (item as ClassLecture)
            classLectureId = classLecture.id
            lectureReviewDetailViewModel.fetchDialogData(lectureReviewDetailViewModel.semester.value?.id?.toLong()!!, classLecture.id)
            //lectureReviewDetailViewModel.setDialogCheckButton(classLecture.id)


        }
    }
    private val checkClickListener = object : RecyclerViewClickListener {
        override fun onClick(view: View, position: Int, item: Any) {
            var timeTable = (item as TimeTable)
            timeTableId = timeTable.id
            if (timeTable.isChecked) {
                timeTable.isChecked = false
                //timeTableCheckList.remove(timeTableId)
                listDialogRecyclerViewAdapter.notifyItemChanged(position)
                lectureReviewDetailViewModel.deleteLectureInTimeTable(classLectureId, timeTableId)
            } else {
                //timeTableCheckList.add(timeTableId)
                timeTable.isChecked = true
                listDialogRecyclerViewAdapter.notifyItemChanged(position)
                lectureReviewDetailViewModel.addLectureInTimeTable(classLectureId, timeTableId)

            }
        }
    }

    /* 신고하기 리스너 */
    private val reportClickListener = object : RecyclerViewClickListener {
        override fun onClick(view: View, position: Int, item: Any) {
            val reportDialog = AlertDialog.Builder(
                requireContext(),
                android.R.style.Theme_Material_Light_Dialog_Alert
            )
            reportDialog.setItems(reportList, DialogInterface.OnClickListener { dialog, which ->
                val reportRequest =
                    LectureReviewReportRequest((item as LectureReview).id, which + 1)
                lectureReviewDetailViewModel.reportLectureReview(reportRequest)
            })
                .setCancelable(true)
                .show()
        }
    }

    /* 업지버튼 리스너 */
    private val recyclerViewClickListener = object : RecyclerViewClickListener {
        override fun onClick(view: View, position: Int, item: Any) {
            lectureReviewDetailViewModel.postReviewRecommend((item as LectureReview).id)
            if (item.isLiked) {
                item.isLiked = false
                item.likes -= 1
            } else {
                item.isLiked = true
                item.likes += 1
            }
            //item.isLiked = !(item as LectureReview).isLiked

            lectureReviewDetailViewModel.commonResponse.observe(viewLifecycleOwner, {
                it?.let {
                    if (it.httpStatus == "OK") {
                        lectureDetailReviewAdapter.notifyItemChanged(position)
                    }
                }

            })
        }
    }
    private val lectureReviewDetailViewModel: LectureReviewDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lecture = arguments?.getParcelable<RankingLectureItem>("lecture")!!
        binding.lecture = lecture
        lectureReviewDetailViewModel.saveRecentlyReadLectureReviews(lecture)
        //lecture?.id?.let { lectureReviewDetailViewModel.getEvaluationRating(it) }
        init()
        initViewModel()
        initEvent()

    }


    private fun initViewModel() {
        with(lectureReviewDetailViewModel) {
            classLectureList.observe(viewLifecycleOwner, {
                it?.let {
                    lectureClassTimeAdapter.submitList(it)
                    hideProgressDialog()
                }
            })
            chartList.observe(viewLifecycleOwner, {
                binding.lectureDetailBarchart.initScoreChart(requireContext(), it)
            })
            evaluationTotal.observe(viewLifecycleOwner, {
                it?.let {
                    binding.evaluation = it
                }
            })
            recommendedDocs.observe(viewLifecycleOwner, {
                it?.let {
                    if (it.size == 0) {
                        binding.lectureDetailNoRecommendDocsGuide.visibility = View.VISIBLE
                    } else {
                        binding.lectureDetailNoRecommendDocsGuide.visibility = View.INVISIBLE
                        recommendedDocsAdapter.submitList(it)
                    }
                }
            })
            reviewList.observe(viewLifecycleOwner, {
                it?.let {
                    lectureDetailReviewAdapter.submitData(lifecycle, it)
                }
            })

            reportResult.observe(viewLifecycleOwner, {
                if (it.httpStatus == "OK") {
                    makeReportResultDialog(
                        requireContext().getString(R.string.report_dialog_title),
                        requireContext().getString(R.string.report_dialog_messge)
                    )
                } else {
                    makeReportResultDialog(
                        requireContext().getString(R.string.report_dialog_title),
                        it.message!!
                    )
                }
            })
            userTimeTableList.observe(viewLifecycleOwner, {
                it.let {
                    listDialogRecyclerViewAdapter.submitList(it)
                    timeTableListDialog = TimeTableListDialog(
                        listDialogRecyclerViewAdapter,
                        timeTableListDialogClickListener
                    )
                    timeTableListDialog.show(parentFragmentManager, "TimetTableListDialog")
                }
            })
            reviewCount.observe(viewLifecycleOwner, {
                it.let { binding.lectureDetailPersonalEvaluation.text = it.toString() }
            })
            isLoading.observe(viewLifecycleOwner, {
                if (it) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            })
            semester.observe(viewLifecycleOwner, { semester ->
                lecture.id.let { lectureId ->
                    lectureReviewDetailViewModel.fetchClassLectureList(lectureId, semester.id.toLong())
                }
            })

        }
    }

    /* 신고 완료 다이얼로그 생성 */
    private fun makeReportResultDialog(title: String, message: String) {
        DialogUtil.makeSimpleDialog(requireContext(),
            title,
            message,
            requireContext().getString(R.string.ok),
            null,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }

            },
            null,
            true
        ).show()
    }

    private fun initEvent() {
        binding.lectureDetailAppbar.setRightButtonClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(activity, LectureEvaluationActivity::class.java)
                intent.putExtra("lectureId", lecture.id)
                intent.putExtra("lectureName", lecture.name)
                startActivity(intent)
            }
        })
        binding.lectureDetailAppbar.backButtonOnClickListener = (object : View.OnClickListener {
            override fun onClick(v: View?) {
                navController.navigate(R.id.action_lecture_review_detail_to_navigation_evaluation)
            }
        })
        binding.lectureDetailOrderByLike.setOnClickListener {
            lectureReviewDetailViewModel.sort = lectureReviewDetailViewModel.SORT_BY_LIKE_COUNT
            binding.lectureDetailSortType.text = lectureReviewDetailViewModel.sort
            binding.lectureDetailOrderPopup.visibility = View.GONE
            lectureReviewDetailViewModel.getReviewList(
                lecture.id,
                lectureReviewDetailViewModel.keyword,
                lectureReviewDetailViewModel.sort
            )
        }
        binding.lectureDetailOrderByLatest.setOnClickListener {
            lectureReviewDetailViewModel.sort = lectureReviewDetailViewModel.SORT_BY_DATE_LATEST
            binding.lectureDetailSortType.text = lectureReviewDetailViewModel.sort
            binding.lectureDetailOrderPopup.visibility = View.GONE
            lectureReviewDetailViewModel.getReviewList(
                lecture.id,
                lectureReviewDetailViewModel.keyword,
                lectureReviewDetailViewModel.sort
            )
        }
        binding.lectureDetailSortType.setOnClickListener {
            binding.lectureDetailOrderPopup.visibility = View.VISIBLE
        }
        binding.lectureDetailSortTypeIcon.setOnClickListener {
            binding.lectureDetailOrderPopup.visibility = View.VISIBLE
        }
        binding.lectureDetailBookmark.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                lectureReviewDetailViewModel.postScrap(lecture.id)
            else
                lectureReviewDetailViewModel.deleteScrap(lecture.id)
        }


    }


    private fun init() {
        lectureClassTimeAdapter = LectureClassTimeAdapter(requireContext())
        binding.lectureDetailClassTimeRecyclerview.adapter = lectureClassTimeAdapter
        binding.lectureDetailRecommendRecyclerview.adapter = recommendedDocsAdapter
        binding.lectureDetailReviewRecyclerview.adapter = lectureDetailReviewAdapter
        lectureDetailReviewAdapter.setRecyclerViewListener(recyclerViewClickListener)
        lectureDetailReviewAdapter.setReportClickListener(reportClickListener)
        lectureClassTimeAdapter.setClassClickListener(classClickListener)
        listDialogRecyclerViewAdapter.setCheckClickListener(checkClickListener)
        recommendedDocsAdapter.setLectureDocClickListener(lectureDocsItemClickListener)
        binding.lectureDetailSortType.text = lectureReviewDetailViewModel.sort
        lectureReviewDetailViewModel.getSemesterId()
        lecture.id.let {
            lectureReviewDetailViewModel.getEvaluationRating(it)
            lectureReviewDetailViewModel.getEvaluationTotal(it)
            lectureReviewDetailViewModel.getReviewList(
                it,
                lectureReviewDetailViewModel.keyword,
                lectureReviewDetailViewModel.sort
            )
            lectureReviewDetailViewModel.getPersonalReviewCount(
                it,
                lectureReviewDetailViewModel.keyword,
                lectureReviewDetailViewModel.sort
            )
        }
        lecture.name.let {
            lectureReviewDetailViewModel.getRecommentedDocs(it)
        }
        lecture.isScraped.let {
            binding.lectureDetailBookmark.isChecked = it
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigate(R.id.action_lecture_review_detail_to_navigation_evaluation)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback);
    }





}
