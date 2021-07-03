package `in`.hangang.hangang.ui.settings.activity

import `in`.hangang.core.base.activity.ViewBindingActivity
import `in`.hangang.core.util.DialogUtil
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.ActivityMyProfileBinding
import `in`.hangang.hangang.ui.settings.viewmodel.MyProfileActivityViewModel
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyProfileActivity : ViewBindingActivity<ActivityMyProfileBinding>() {
    override val layoutId = R.layout.activity_my_profile
    private val myProfileViewModel: MyProfileActivityViewModel by viewModel()
    private var EDITBUTTON_VISIBLE = 0
    private var EDITBUTTON_GONE = 1
    var majorClickListener: AdapterView.OnItemClickListener? =
        AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
            var selectedMajor = resources.getTextArray(R.array.major_list).toList()[p2]
            binding.profileMajorSecond.text = selectedMajor
            if (selectedMajor != myProfileViewModel.bottomSheetSelectedMajorList[0]) {
                majorSecondViews(true)
                binding.addMajor.isVisible = false
                myProfileViewModel.bottomSheetSelectedMajorList.add(selectedMajor.toString())
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myProfileViewModel.init()
        initView()
        binding.vm = myProfileViewModel

    }

    private fun initView() {
        initAppBar()
        initViewModel()

    }

    private fun initViewModel() {
        with(binding) {
            myProfileViewModel.myProfile.observe(this@MyProfileActivity) {
                it.let {
                    nameText.text = it.name?.toEditable()
                    profileIdText.text = it.portalAccount
                    profileNicknameEditText.text = it.nickname.toEditable()
                    myProfileViewModel.nickName = it.nickname
                    if (it.major.size == 1) {
                        profileMajorFirst.text = it.major[0]
                    } else {
                        profileMajorSecond.isVisible = true
                        profileMajorFirst.text = it.major[0]
                        profileMajorSecond.text = it.major[1]
                        underbarMajorSecond.isVisible = true
                    }
                    myProfileViewModel.bottomSheetSelectedMajorList.addAll(it.major)
                }
            }
            addMajor.setOnClickListener {
                DialogUtil.makeListBottomSheet(
                    this@MyProfileActivity,
                    resources.getTextArray(R.array.major_list).toList(), null, majorClickListener
                ).show()
            }
            binding.profileMajorSecondDelete.setOnClickListener {
                myProfileViewModel.bottomSheetSelectedMajorList.removeAt(1)
                majorSecondViews(false)
            }

            myProfileViewModel.isProfileEdited.observe(this@MyProfileActivity) {
                if (it) {
                    editButtonVisibility(EDITBUTTON_GONE)
                    underBarColorEdit(R.color.blue_200)
                    editButtonView(true)
                    binding.profileMajorSecondDelete.isVisible = true
                    if (myProfileViewModel.bottomSheetSelectedMajorList.size == 1) {
                        addMajor.isVisible = true
                        binding.profileMajorSecondDelete.isVisible = false

                    }
                } else {
                    editButtonVisibility(EDITBUTTON_VISIBLE)
                    underBarColorEdit(R.color.gray_100)
                    editButtonView(false)
                    binding.profileMajorSecondDelete.isVisible = false
                }
            }
        }
    }

    private fun initAppBar() {
        editButton.setTextColor(ContextCompat.getColor(this, R.color.blue_200))
        finishButton.setTextColor(ContextCompat.getColor(this, R.color.blue_200))
        with(binding.appBar) {
            addViewInRight(editButton)
            addViewInRight(finishButton)
            setOnAppBarButtonClickListener({ _, _ -> }, { view, _ ->
                when (view) {
                    editButton -> myProfileViewModel.setEditMode(true)
                    finishButton -> if (myProfileViewModel.isProfileEdited.value == true) {
                        if (myProfileViewModel.nickName != binding.profileNicknameEditText.toString()){
                        myProfileViewModel.applyMyProfile(
                            binding.nameText.text.toString(),
                            binding.profileNicknameEditText.text.toString(),
                            myProfileViewModel.bottomSheetSelectedMajorList
                        )}else{
                            myProfileViewModel.applyMyProfileWithNickname(
                                binding.nameText.text.toString(),
                                binding.profileNicknameEditText.text.toString(),
                                myProfileViewModel.bottomSheetSelectedMajorList
                            )
                        }
                        myProfileViewModel.nickNameEditStatus.observe(this@MyProfileActivity) {
                            if (myProfileViewModel.nickNameEditStatus.value == true) {
                                nickNameError(true)
                            } else {
                                nickNameError(false)
                            }
                        }
                    }
                }
            })
        }
    }

    private val editButton: Button by lazy {
        appBarTextButton(
            getString(R.string.profile_edit_button)
        )
    }
    private val finishButton: Button by lazy {
        appBarTextButton(
            getString(R.string.profile_done_button)
        )
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun underBarColorEdit(id: Int) {
        binding.underbarName.setBackgroundColor(ContextCompat.getColor(applicationContext, id))
        binding.underbarNickname.setBackgroundColor(ContextCompat.getColor(applicationContext, id))
        binding.underbarMajorFirst.setBackgroundColor(ContextCompat.getColor(applicationContext, id))
        binding.underbarMajorSecond.setBackgroundColor(ContextCompat.getColor(applicationContext, id))
    }

    private fun nickNameError(state: Boolean) {
        binding.profileNicknameError.isVisible = state
        binding.nicknameErrorImage.isVisible = state
    }

    private fun majorSecondViews(state: Boolean) {
        binding.profileMajorSecond.isVisible = state
        binding.underbarMajorSecond.isVisible = state
        binding.profileMajorSecondDelete.isVisible = state
    }

    private fun editButtonView(state: Boolean) {
        binding.nameText.isEnabled = state
        binding.profileNicknameEditText.isEnabled = state
    }

    private fun editButtonVisibility(view: Int) {
        if (view == 1) {
            editButton.visibility = View.GONE
            finishButton.visibility = View.VISIBLE
        } else {
            editButton.visibility = View.VISIBLE
            finishButton.visibility = View.GONE
        }

    }


}