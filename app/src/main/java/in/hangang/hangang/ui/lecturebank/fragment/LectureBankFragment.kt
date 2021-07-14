package `in`.hangang.hangang.ui.lecturebank.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.constant.DEPARTMENT_COMPUTER
import `in`.hangang.hangang.constant.DEPARTMENT_CONVERGENCE
import `in`.hangang.hangang.constant.DEPARTMENT_ELECTRONIC
import `in`.hangang.hangang.constant.DEPARTMENT_HRD
import `in`.hangang.hangang.constant.DEPARTMENT_LIBERAL
import `in`.hangang.hangang.constant.DEPARTMENT_MECHANICAL
import `in`.hangang.hangang.constant.DEPARTMENT_DESIGN
import `in`.hangang.hangang.constant.DEPARTMENT_MECHATRONICS
import `in`.hangang.hangang.constant.DEPARTMENT_INDUSTRIAL
import `in`.hangang.hangang.constant.DEPARTMENT_ENERGY
import `in`.hangang.hangang.databinding.FragmentLectureBankBinding
import `in`.hangang.hangang.ui.lecturebank.adapter.LectureBankListAdapter
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankDetailActivityContract
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankEditorActivityContract
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankFilterActivityContract
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class LectureBankFragment : ViewBindingFragment<FragmentLectureBankBinding>() {
    override val layoutId = R.layout.fragment_lecture_bank

    private val departmentMap: Map<CheckBox, String> by lazy {
        mapOf(
            binding.radioButtonMajor0 to DEPARTMENT_HRD,
            binding.radioButtonMajor1 to DEPARTMENT_LIBERAL,
            binding.radioButtonMajor2 to DEPARTMENT_MECHANICAL,
            binding.radioButtonMajor3 to DEPARTMENT_DESIGN,
            binding.radioButtonMajor4 to DEPARTMENT_MECHATRONICS,
            binding.radioButtonMajor5 to DEPARTMENT_INDUSTRIAL,
            binding.radioButtonMajor6 to DEPARTMENT_ENERGY,
            binding.radioButtonMajor7 to DEPARTMENT_CONVERGENCE,
            binding.radioButtonMajor8 to DEPARTMENT_ELECTRONIC,
            binding.radioButtonMajor9 to DEPARTMENT_COMPUTER
        )
    }

    private val lectureBankViewModel: LectureBankViewModel by viewModel()
    private val lectureBankListAdapter by lazy { LectureBankListAdapter() }

    private val lectureBankFilterActivityContract = registerForActivityResult(LectureBankFilterActivityContract()) {
        if (it != null) {
            lectureBankViewModel.setFilter(it)
            binding.recyclerViewLectureBank.smoothScrollToPosition(0)
        }
    }

    private val lectureBankEditorActivityContract = registerForActivityResult(LectureBankEditorActivityContract()) {
        when(it) {
            LectureBankEditorActivityContract.RESULT_UPLOADED -> lectureBankViewModel.getLectureBanks()
        }
    }

    private val lectureBankDetailActivityContract = registerForActivityResult(LectureBankDetailActivityContract()) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()

        lectureBankViewModel.getLectureBanks()
    }

    private fun initViewModel() {
        with(lectureBankViewModel) {
            lectureBankPagingData.observe(viewLifecycleOwner) {
                lectureBankListAdapter.submitData(lifecycle, it)
            }
            isLectureBankLoading.observe(viewLifecycleOwner) {
                binding.lectureBankSwipeRefreshLayout.isRefreshing = it
            }
        }
    }

    private fun initView() {
        val lectureDocId = arguments?.getInt("LECTURE_DOC_ID")?:null
        if(lectureDocId != null){
            lectureBankDetailActivityContract.launch(lectureDocId)
        }
        with(binding.recyclerViewLectureBank) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = lectureBankListAdapter
        }

        binding.appBar.setSearchListener {
            lectureBankViewModel.setKeyword(it)
            lectureBankViewModel.getLectureBanks()
        }

        binding.lectureBankSwipeRefreshLayout.setOnRefreshListener {
            lectureBankViewModel.getLectureBanks()
        }

        binding.buttonLectureBankLectureFilter.setOnClickListener {
            lectureBankFilterActivityContract.launch(lectureBankViewModel.lectureBankFilter.value)
        }

        departmentMap.keys.forEach {
            it.setOnClickListener { _ ->
                if (it.isChecked) {
                    departmentMap.keys.forEach { key -> key.isChecked = false }
                    it.isChecked = true
                }
                lectureBankViewModel.setDepartment(if (it.isChecked) departmentMap[it] else null)
                binding.recyclerViewLectureBank.smoothScrollToPosition(0)
            }
        }

        with(lectureBankListAdapter) {
            setOnItemClickListener {
                lectureBankDetailActivityContract.launch(it)
            }
            viewLifecycleOwner.lifecycleScope.launch {
                loadStateFlow.collectLatest {
                        if(it.refresh is LoadState.NotLoading){
                            binding.recyclerViewEmptyView.isVisible = itemCount < 1
                        }
                    }
            }
        }

        binding.buttonNewLectureBank.setOnClickListener {
            lectureBankEditorActivityContract.launch(null)
        }
    }
}