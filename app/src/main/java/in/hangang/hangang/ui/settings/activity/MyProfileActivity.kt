package `in`.hangang.hangang.ui.settings.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityMyProfileBinding
import `in`.hangang.hangang.ui.settings.viewmodel.MyProfileActivityViewModel
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.layout_simple_list_bottom_sheet.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyProfileActivity : ViewBindingActivity<ActivityMyProfileBinding>() {
    override val layoutId = R.layout.activity_my_profile
    private val myProfileViewModel: MyProfileActivityViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = myProfileViewModel
        initView()
        myProfileViewModel.init()
    }

    private fun initView() {
        initAppBar()
        initObserver()

    }

    private fun initObserver() {
        with(binding) {
            myProfileViewModel.myProfile.observe(this@MyProfileActivity) {
                nameText.setText(vm.myProfile.value?.name)
                profileIdText.text = vm.myProfile.value!!.portalAccount
                profileNicknameEditText.setText(vm.myProfile.value!!.nickname)
                profileMajorTextview.text = vm.myProfile.value!!.major
            }
            addMajor.setOnClickListener{
                DialogUtil.makeListBottomSheet(this@MyProfileActivity, items)
            }

        }
    }

    private fun initAppBar() {
        val editButton = TextView(this)
        editButton.text = "수정"
        editButton.setTextColor(Color.parseColor("#238bfe"))
        editButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        editButton.isEnabled = false
        with(binding.appBar) {
            addViewInRight(editButton)
            setOnAppBarButtonClickListener(
                onClickViewInRightContainer = { view, index ->
                    with(binding.vm) {
                        appBarRightButton.observe(this@MyProfileActivity) {
                            when (it) {
                                false -> {
                                    editButton.text = "완료"
                                    editButton.isEnabled = true
                                    binding.addMajor.visibility = View.VISIBLE
                                }
                                true -> {
                                    editButton.text = "수정"
                                    applyMyProfile(
                                        binding.nameText.text.toString(),
                                        binding.profileNicknameEditText.text.toString(),
                                        majorArray.plus(binding.profileMajorTextview.text.toString())
                                    )
                                    editButton.isEnabled = false


                                }
                            }


                        }
                    }
                }
            )
        }
    }
}