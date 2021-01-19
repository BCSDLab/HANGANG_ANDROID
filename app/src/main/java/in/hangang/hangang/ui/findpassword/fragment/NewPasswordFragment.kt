package `in`.hangang.hangang.ui.findpassword.fragment

import `in`.hangang.core.base.fragment.ViewBindingFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentNewPasswordBinding
import `in`.hangang.hangang.ui.findpassword.viewmodel.NewPasswordViewModel
import android.text.Editable
import android.text.TextWatcher

class NewPasswordFragment : ViewBindingFragment<FragmentNewPasswordBinding>(R.layout.fragment_new_password) {

    val newPasswordViewModel : NewPasswordViewModel by viewModel()

    private val newPasswordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            newPasswordViewModel.newPassword.postValue(s.toString())
        }
    }
    private val newPasswordConfirmTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            newPasswordViewModel.newPasswordConfirm.postValue(s.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            editTextNewPassword.addTextChangedListener(newPasswordTextWatcher)
            editTextConfirmNewPassword.addTextChangedListener(newPasswordConfirmTextWatcher)
        }
        with(newPasswordViewModel) {
            newPassword.observe(viewLifecycleOwner) {
                binding.canFinishChangePassword = it == newPasswordConfirm.value
            }
            newPasswordConfirm.observe(viewLifecycleOwner) {
                binding.canFinishChangePassword = it == newPassword.value
            }
            passwordRegexErrorMessage.observe(viewLifecycleOwner) {
                binding.passwordRegexErrorMessage = it
            }
        }
    }

    fun onClickApplyNewPassword(view: View) {
        newPasswordViewModel.applyNewPassword()
    }

}