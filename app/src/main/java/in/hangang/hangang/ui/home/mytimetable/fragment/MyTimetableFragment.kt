package `in`.hangang.hangang.ui.home.mytimetable.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.recyclerview.RecyclerViewClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.data.entity.LectureTimeTable
import `in`.hangang.hangang.data.ranking.RankingLectureItem
import `in`.hangang.hangang.databinding.FragmentHomeMyTimetableBinding
import `in`.hangang.hangang.ui.home.mytimetable.adapter.MyTimetableAdapter
import `in`.hangang.hangang.ui.home.mytimetable.viewmodel.MyTimetableFragmentViewModel
import `in`.hangang.hangang.ui.lecturereview.activity.LectureEvaluationActivity
import `in`.hangang.hangang.util.LogUtil
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxjava3.core.CompletableOnSubscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyTimetableFragment : ViewBindingFragment<FragmentHomeMyTimetableBinding>() {
    override val layoutId = R.layout.fragment_home_my_timetable
    private val navController: NavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private var moveType: MoveType = MoveType.READ
    private val myTimetableFragmentViewModel: MyTimetableFragmentViewModel by viewModel()
    private val myTimeTableAdapter = MyTimetableAdapter()
    private val timeTableItemClickListener = object : RecyclerViewClickListener {
        override fun onClick(view: View, position: Int, item: Any) {
            if(view is ConstraintLayout) {
                moveType = MoveType.READ
                if(myTimetableFragmentViewModel.lecture.value == null) {
                    var id = (item as LectureTimeTable).lectureId
                    myTimetableFragmentViewModel.getLecturesId(id)
                } else {
                    goToRead(myTimetableFragmentViewModel.lecture.value!!)
                }
            } else {
                moveType = MoveType.WRITE
                if(myTimetableFragmentViewModel.lecture.value == null) {
                    var id = (item as LectureTimeTable).lectureId
                    myTimetableFragmentViewModel.getLecturesId(id)
                } else {
                    goToWrite(myTimetableFragmentViewModel.lecture.value!!)
                }

            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        subscribe()
        initEvent()

    }

    private fun init() {
        binding.recyclerViewMyTimetable.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewMyTimetable.adapter = myTimeTableAdapter
        myTimeTableAdapter.setClickListener(timeTableItemClickListener)
        myTimetableFragmentViewModel.getMainTimeTable()
    }

    private fun subscribe() {
        myTimetableFragmentViewModel.mainTimetableList.observe(viewLifecycleOwner, {
            myTimeTableAdapter.submitList(it)
        })
        myTimetableFragmentViewModel.mainTimetable.observe(viewLifecycleOwner, {
            if (it.lectureList.isEmpty())
                binding.recyclerViewEmpty.visibility = View.VISIBLE
            else
                binding.recyclerViewEmpty.visibility = View.INVISIBLE
        })
        myTimetableFragmentViewModel.lecture.observe(viewLifecycleOwner, {
            it.let {
                if(moveType == MoveType.READ) {
                    goToRead(it)
                } else {
                    goToWrite(it)
                }
            }
        })
    }

    private fun initEvent() {
        binding.homeEmptyTimetableButtonNewTimetable.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_navigation_evaluation)
        }
    }
    private fun goToRead(item: RankingLectureItem) {
        val bundle = Bundle()
        bundle.putParcelable("lecture", item)
        navController.navigate(
            R.id.action_navigation_home_to_lecture_review_detail,
            bundle
        )
    }
    private fun goToWrite(item: RankingLectureItem) {
        val intent = Intent(activity, LectureEvaluationActivity::class.java)
        intent.putExtra("lectureId",item.id)
        intent.putExtra("lectureName",item.name)
        startActivity(intent)
    }
    enum class MoveType{
        READ, WRITE
    }
}