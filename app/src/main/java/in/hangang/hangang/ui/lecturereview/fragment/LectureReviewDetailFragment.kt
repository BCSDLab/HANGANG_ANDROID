package `in`.hangang.hangang.ui.lecturereview.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.evaluation.Chart
import `in`.hangang.hangang.data.evaluation.LectureReview
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.data.request.LectureReviewReportRequest
import `in`.hangang.hangang.data.source.ReviewPagingSource.Companion.LECTURE_REVIEW_TOTAL
import `in`.hangang.hangang.databinding.FragmentLectureReviewDetailBinding
import `in`.hangang.hangang.ui.lecturereview.activity.LectureEvaluationActivity
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureClassTimeAdapter
import `in`.hangang.hangang.ui.lecturereview.adapter.LectureDetailReviewAdapter
import `in`.hangang.hangang.ui.lecturereview.adapter.RecommendedDocsAdapter
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewDetailViewModel
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewListViewModel
import `in`.hangang.hangang.util.LogUtil
import `in`.hangang.hangang.util.bindImageFromUrl
import `in`.hangang.hangang.util.initScoreChart
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.github.mikephil.charting.data.BarEntry
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureReviewDetailFragment : ViewBindingFragment<FragmentLectureReviewDetailBinding>() {
    override val layoutId = R.layout.fragment_lecture_review_detail
    lateinit var lecture: RankingLectureItem
    private val lectureClassTimeAdapter = LectureClassTimeAdapter()
    private val recommendedDocsAdapter = RecommendedDocsAdapter()
    private val lectureDetailReviewAdapter = LectureDetailReviewAdapter()
    private val reportList = arrayOf("욕설/비하", "유출/사칭/저작권 위배", "허위/부적절한 정보", "광고/도배", "음란물")
    private val navController: NavController by lazy {
        Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
    }
    private val reportClickListener = object : RecyclerViewClickListener{
        override fun onClick(view: View, position: Int, item: Any) {
            val reportDialog = AlertDialog.Builder(requireContext(),android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
            reportDialog.setItems(reportList, DialogInterface.OnClickListener { dialog, which ->
                LogUtil.e(which.toString())
                val reportRequest = LectureReviewReportRequest((item as LectureReview).id, which+1)
                lectureReviewDetailViewModel.reportLectureReview(reportRequest)
            })
                .setCancelable(true)
                .show()
        }
    }
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
        //lecture?.id?.let { lectureReviewDetailViewModel.getEvaluationRating(it) }
        init()
        initViewModel()
        initEvent()
    }

    fun initViewModel() {
        with(lectureReviewDetailViewModel) {
            classLectureList.observe(viewLifecycleOwner, {
                it?.let { lectureClassTimeAdapter.submitList(it) }
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
                    binding.lectureDetailPersonalEvaluation.text = LECTURE_REVIEW_TOTAL.toString()
                }
            })
            isLoading.observe(viewLifecycleOwner, {
                if (it) {
                    LogUtil.e(it.toString())
                    showProgressDialog()
                } else
                    hideProgressDialog()
            })
            reportResult.observe(viewLifecycleOwner,{
                if(it.httpStatus == "OK"){
                    makeReportResultDialog(requireContext().getString(R.string.report_dialog_title),requireContext().getString(R.string.report_dialog_messge))
                }else{
                    makeReportResultDialog(requireContext().getString(R.string.report_dialog_title), it.message!!)
                }
            })

        }
    }
    fun makeReportResultDialog(title: String, message: String){
        DialogUtil.makeSimpleDialog(requireContext(),title,message,requireContext().getString(R.string.ok),
            null, object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }

            }, null, true).show()
    }

    fun initEvent() {
        binding.lectureDetailAppbar.setRightButtonClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                LogUtil.e("click")
                val intent = Intent(activity, LectureEvaluationActivity::class.java)
                intent.putExtra("lectureId",lecture.id)
                intent.putExtra("lectureName",lecture.name)
                startActivity(intent)
            }
        })
        binding.lectureDetailOrderByLike.setOnClickListener {
            lectureReviewDetailViewModel.sort = lectureReviewDetailViewModel.SORT_BY_LIKE_COUNT
            binding.lectureDetailSortType.text = lectureReviewDetailViewModel.sort
            binding.lectureDetailOrderPopup.visibility = View.GONE
            lectureReviewDetailViewModel.getReviewList(lecture.id, lectureReviewDetailViewModel.keyword, lectureReviewDetailViewModel.sort)
        }
        binding.lectureDetailOrderByLatest.setOnClickListener {
            lectureReviewDetailViewModel.sort = lectureReviewDetailViewModel.SORT_BY_DATE_LATEST
            binding.lectureDetailSortType.text = lectureReviewDetailViewModel.sort
            binding.lectureDetailOrderPopup.visibility = View.GONE
            lectureReviewDetailViewModel.getReviewList(lecture.id, lectureReviewDetailViewModel.keyword, lectureReviewDetailViewModel.sort)
        }
        binding.lectureDetailSortType.setOnClickListener {
            binding.lectureDetailOrderPopup.visibility = View.VISIBLE
        }
        binding.lectureDetailSortTypeIcon.setOnClickListener {
            binding.lectureDetailOrderPopup.visibility = View.VISIBLE
        }


    }


    fun init() {
        binding.lectureDetailClassTimeRecyclerview.adapter = lectureClassTimeAdapter
        binding.lectureDetailRecommendRecyclerview.adapter = recommendedDocsAdapter
        binding.lectureDetailReviewRecyclerview.adapter = lectureDetailReviewAdapter
        lectureDetailReviewAdapter.setRecyclerViewListener(recyclerViewClickListener)
        lectureDetailReviewAdapter.setReportClickListener(reportClickListener)
        binding.lectureDetailSortType.text = lectureReviewDetailViewModel.sort
        lecture.id.let {
            lectureReviewDetailViewModel.getClassLectureList(it)
            lectureReviewDetailViewModel.getEvaluationRating(it)
            lectureReviewDetailViewModel.getEvaluationTotal(it)
            lectureReviewDetailViewModel.getReviewList(
                it,
                lectureReviewDetailViewModel.keyword,
                lectureReviewDetailViewModel.sort
            )
        }
        lecture.name.let {
            lectureReviewDetailViewModel.getRecommentedDocs(it)
        }
        /*
        var list: ArrayList<BarEntry> = ArrayList()
        list.add(BarEntry(1f, 30f))
        list.add(BarEntry(2f, 10f))
        list.add(BarEntry(3f, 20f))
        list.add(BarEntry(4f, 30f))
        list.add(BarEntry(5f, 40f))
        list.add(BarEntry(6f, 50f))
        list.add(BarEntry(7f, 60f))
        list.add(BarEntry(8f, 70f))
        list.add(BarEntry(9f, 80f))
        list.add(BarEntry(10f, 90f))
        binding.lectureDetailBarchart.initScoreChart(requireContext(), list)

         */

    }
}
