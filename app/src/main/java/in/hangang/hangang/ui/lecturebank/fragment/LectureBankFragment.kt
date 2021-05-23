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
import `in`.hangang.hangang.ui.lecturebank.contract.LectureBankFilterActivityContract
import `in`.hangang.hangang.ui.lecturebank.viewmodel.LectureBankViewModel
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.jvm.internal.impl.util.Check

class LectureBankFragment : ViewBindingFragment<FragmentLectureBankBinding>() {
    override val layoutId = R.layout.fragment_lecture_bank

    private val departmentMap : Map<CheckBox, String> by lazy{ mapOf(
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
    )}

    private val lectureBankViewModel : LectureBankViewModel by viewModel()
    private val lectureBankListAdapter by lazy { LectureBankListAdapter() }

    private val lectureBankFilterActivityContract = registerForActivityResult(LectureBankFilterActivityContract()) {
        if(it != null) {
            lectureBankViewModel.setFilter(it)
        }
    }

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
        }
    }

    private fun initView() {
        with(binding.recyclerViewLectureBank) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = lectureBankListAdapter
        }

        binding.buttonLectureBankLectureFilter.setOnClickListener {
            lectureBankFilterActivityContract.launch(lectureBankViewModel.lectureBankFilter.value)
        }

        departmentMap.keys.forEach { it.setOnClickListener { _ ->
            if(it.isChecked) {
                departmentMap.keys.forEach { key -> key.isChecked = false }
                it.isChecked = true
            }
            lectureBankViewModel.setDepartment(if(it.isChecked) departmentMap[it] else null)
        }}
    }
}