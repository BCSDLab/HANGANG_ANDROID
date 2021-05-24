package `in`.hangang.hangang.ui.lecturereview.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityLectureEvaluationBinding
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureEvaluationViewModel
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewDetailViewModel
import `in`.hangang.hangang.util.LogUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LectureEvaluationActivity : ViewBindingActivity<ActivityLectureEvaluationBinding>() {
    override val layoutId = R.layout.activity_lecture_evaluation
    private val lectureEvaluationViewModel: LectureEvaluationViewModel by viewModel()
    private var lectureId: Int = -1
    val INTENT_KEY = "lectureId"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lectureId = intent.getIntExtra(INTENT_KEY, 0)
        initViewModel()
        init()
    }
    fun initViewModel(){
        with(lectureEvaluationViewModel){
            semesterLectureList.observe(this@LectureEvaluationActivity,{
                binding.lectureEvaluationSemesterSpinner.items = it
            })
        }
    }
    fun init(){
        lectureEvaluationViewModel.getLectureSemester(lectureId)
        LogUtil.e(binding.lectureEvaluationRatingBar.rating.toString())

        binding.lectureEvaluationRatingBar.rating = 0.5f
        binding.lectureEvaluationRatingBar.setOnRatingBarChangeListener(LectureEvaluateRatingbarChangeListener())

    }
    inner class LectureEvaluateRatingbarChangeListener: RatingBar.OnRatingBarChangeListener{
        override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
            if(binding.lectureEvaluationRatingBar.rating < 0.5f){
                binding.lectureEvaluationRatingBar.rating = rating
            }
        }
    }
}