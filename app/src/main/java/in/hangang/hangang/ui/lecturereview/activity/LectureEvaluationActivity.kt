package `in`.hangang.hangang.ui.lecturereview.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.toast.shortToast
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.view.button.RoundedCornerButton
import `in`.hangang.core.view.button.checkbox.FilledCheckBox
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.request.LectureEvaluationIdRequest
import `in`.hangang.hangang.databinding.ActivityLectureEvaluationBinding
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureEvaluationViewModel
import `in`.hangang.hangang.ui.lecturereview.viewmodel.LectureReviewDetailViewModel
import `in`.hangang.hangang.util.LogUtil
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.RatingBar
import androidx.core.widget.addTextChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class LectureEvaluationActivity : ViewBindingActivity<ActivityLectureEvaluationBinding>() {
    override val layoutId = R.layout.activity_lecture_evaluation
    private val lectureEvaluationViewModel: LectureEvaluationViewModel by viewModel()
    private var lectureId: Int = -1
    private var lectureName: String = ""
    private val INTENT_KEY = "lectureId"
    private val INTENT_NAME_KEY = "lectureName"
    private var preText = ""
    private var isKeyboardShowing = false;
    private var keypadBaseHeight = 0;

    private val onSemesterClickListener = object : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            lectureEvaluationViewModel.semesterId = lectureEvaluationViewModel.semesterData[position]
        }
    }
    private val hashTagTypeId = arrayOf(
        R.id.lecture_evaluation_hash_tag_type_1,
        R.id.lecture_evaluation_hash_tag_type_2,
        R.id.lecture_evaluation_hash_tag_type_3,
        R.id.lecture_evaluation_hash_tag_type_4,
        R.id.lecture_evaluation_hash_tag_type_5,
        R.id.lecture_evaluation_hash_tag_type_6,
        R.id.lecture_evaluation_hash_tag_type_7,
        R.id.lecture_evaluation_hash_tag_type_8,
        R.id.lecture_evaluation_hash_tag_type_9
    )
    private val hashTagCheckBox = arrayOfNulls<FilledCheckBox>(9)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lectureId = intent.getIntExtra(INTENT_KEY, 0)
        lectureName = intent.getStringExtra(INTENT_NAME_KEY)
        initViewModel()
        init()
        initEvent()
    }
    fun initViewModel(){
        with(lectureEvaluationViewModel){
            semesterLectureList.observe(this@LectureEvaluationActivity,{
                binding.lectureEvaluationSemesterSpinner.items = it
                binding.lectureEvaluationSemesterSpinner.onItemClickListener = onSemesterClickListener
            })
            postCommonResponse.observe(this@LectureEvaluationActivity,{
                shortToast { it.message.toString() }
            })
        }
    }
    fun init(){
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding.lectureEvaluationAppbar.title = lectureName
        lectureEvaluationViewModel.lectureId = lectureId
        binding.root.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

                //뷰가 불러지고 나서 처리할 코드 입력

                var r = Rect() // 키보드 위로 보여지는 공간
                binding.root.getWindowVisibleDisplayFrame(r)
                var screenHeight = binding.root.getRootView().getHeight() // rootView에 대한 전체 높이


                // 키보드가 보여지면 현재 보여지는 rootView의 범위가 전체 rootView의 범위보다 작아지므로 전체 크기에서 현재 보여지는 크기를 빼면 키보드의 크기가 됨.

                var keypadHeight = screenHeight -r.bottom;



                if (keypadBaseHeight == 0) { // 기기마다 소프트 키보드가 구현되는 방식이 다름. 화면 아래에 숨어있거나 invisible로 구현되어 있음. 그차이로 인해 기기마다 약간씩 레이아웃이 틀어지는데 그것을 방지하기 위해 필요함.
                    keypadBaseHeight = keypadHeight
                }


                if (keypadHeight > screenHeight * 0.15) { // 키보드가 대략 전체 화면의 15% 정도 높이 이상으로 올라온다.
                    // 키보드 열렸을 때
                    if (!isKeyboardShowing) {
                        isKeyboardShowing = true
                        //params.height = keypadHeight;
                        //onKeyboardVisibilityChanged(true);

                        binding.root.setPadding(0, 0, 0, keypadHeight)
                        var height = keypadHeight -keypadBaseHeight

                    }
                } else {
                    // 키보드가 닫혔을 때
                    if (isKeyboardShowing) {
                        isKeyboardShowing = false;
                        binding.root.setPadding(0, 0, 0, 0);
                    }
                }

            }
        })



        for(id in 0..8){
            hashTagCheckBox[id] = binding.root.findViewById(hashTagTypeId[id])
        }
        lectureEvaluationViewModel.getLectureSemester(lectureId)
        LogUtil.e(binding.lectureEvaluationRatingBar.rating.toString())

        binding.lectureEvaluationRatingBar.rating = lectureEvaluationViewModel.rating
        binding.lectureEvaluationRatingBar.setOnRatingBarChangeListener(LectureEvaluateRatingbarChangeListener())
        binding.lectureEvaluationAttendanceFrequencyHigh.isChecked = true
        binding.lectureEvaluationHomeworkHigh.isChecked = true
        binding.lectureEvaluationExamDifficultyHigh.isChecked = true
        binding.lectureEvaluationGradePortionHigh.isChecked = true
        binding.lectureEvaluationAssignmentInfo1.isChecked = true
        lectureEvaluationViewModel.assignment.add(1)
        binding.lectureEvaluationHashTagType1.isChecked = true
        lectureEvaluationViewModel.hashTag.add(1)


    }
    inner class LectureEvaluateRatingbarChangeListener: RatingBar.OnRatingBarChangeListener{
        override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
            if(binding.lectureEvaluationRatingBar.rating < 0.5f){
                lectureEvaluationViewModel.rating = rating
            }
        }
    }
    fun initEvent(){
        binding.lectureEvaluationAttendanceFrequencyRadiogroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.lecture_evaluation_attendance_frequency_low -> {
                    lectureEvaluationViewModel.attendanceFrequency = 1
                }
                R.id.lecture_evaluation_attendance_frequency_middle -> {
                    lectureEvaluationViewModel.attendanceFrequency = 2
                }
                R.id.lecture_evaluation_attendance_frequency_high -> {
                    lectureEvaluationViewModel.attendanceFrequency = 3
                }
            }
        }
        binding.lectureEvaluationHomeworkRadiogroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.lecture_evaluation_homework_low -> {
                    lectureEvaluationViewModel.assignmentAmount = 1
                }
                R.id.lecture_evaluation_homework_middle -> {
                    lectureEvaluationViewModel.assignmentAmount = 2
                }
                R.id.lecture_evaluation_homework_high -> {
                    lectureEvaluationViewModel.assignmentAmount = 3
                }
            }
        }
        binding.lectureEvaluationExamDifficultyRadiogroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.lecture_evaluation_exam_difficulty_low -> {
                    lectureEvaluationViewModel.difficulty = 1
                }
                R.id.lecture_evaluation_exam_difficulty_middle -> {
                    lectureEvaluationViewModel.difficulty = 2
                }
                R.id.lecture_evaluation_exam_difficulty_high -> {
                    lectureEvaluationViewModel.difficulty = 3
                }
            }
        }
        binding.lectureEvaluationGradePortionRadiogroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.lecture_evaluation_grade_portion_low -> {
                    lectureEvaluationViewModel.gradePortion = 1
                }
                R.id.lecture_evaluation_grade_portion_middle -> {
                    lectureEvaluationViewModel.gradePortion = 2
                }
                R.id.lecture_evaluation_grade_portion_high -> {
                    lectureEvaluationViewModel.gradePortion = 3
                }
            }
        }
        binding.lectureEvaluationAssignmentInfo1.setOnClickListener {
            if((it as FilledCheckBox).isChecked)
                lectureEvaluationViewModel.assignment.add(1)
            else
                lectureEvaluationViewModel.assignment.remove(1)
        }
        binding.lectureEvaluationAssignmentInfo2.setOnClickListener {
            if((it as FilledCheckBox).isChecked)
                lectureEvaluationViewModel.assignment.add(2)
            else
                lectureEvaluationViewModel.assignment.remove(2)
        }
        binding.lectureEvaluationAssignmentInfo3.setOnClickListener {
            if((it as FilledCheckBox).isChecked)
                lectureEvaluationViewModel.assignment.add(3)
            else
                lectureEvaluationViewModel.assignment.remove(3)
        }
        binding.lectureEvaluationAssignmentInfo4.setOnClickListener {
            if((it as FilledCheckBox).isChecked)
                lectureEvaluationViewModel.assignment.add(4)
            else
                lectureEvaluationViewModel.assignment.remove(4)
        }
        for(i in 0..8){
            hashTagCheckBox[i]?.setOnClickListener {
                if((it as FilledCheckBox).isChecked)
                    lectureEvaluationViewModel.hashTag.add(i+1)
                else
                    lectureEvaluationViewModel.hashTag.remove(i+1)
            }
        }
        binding.lectureEvaluationCompleteButton.setOnClickListener {
            if(it.isEnabled) {
                lectureEvaluationViewModel.comment = binding.lectureEvaluationComment.text.toString()
                lectureEvaluationViewModel.rating = binding.lectureEvaluationRatingBar.rating
                lectureEvaluationViewModel.postEvaluation()
            }
        }
        binding.lectureEvaluationComment.addTextChangedListener {
            if(it.toString().length > 0){
                setGuideMessage(View.INVISIBLE)
                binding.lectureEvaluationCompleteButton.isEnabled = true
            }else{
                setGuideMessage(View.VISIBLE)
                binding.lectureEvaluationCompleteButton.isEnabled = false
            }
        }

    }
    fun setGuideMessage(visible: Int){
        binding.lectureEvaluationExclamationIcon.visibility = visible
        binding.lectureEvaluationExclamationMessage.visibility = visible

    }
}