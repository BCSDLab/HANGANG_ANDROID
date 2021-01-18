package `in`.hangang.hangang.ui.findpassword.fragment

import `in`.hangang.core.base.activity.ActivityBase
import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentEmailAuthenticationBinding
import `in`.hangang.hangang.ui.findpassword.viewmodel.EmailAuthenticationViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View

class EmailAuthenticationFragment : ViewBindingFragment<FragmentEmailAuthenticationBinding>(R.layout.fragment_email_authentication) {

    private val emailAuthenticationViewModel: EmailAuthenticationViewModel by viewModel()

    private val editTextEmailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            emailAuthenticationViewModel.email.postValue(s.toString())
        }
    }
    private val editTextEmailAuthNumberTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            emailAuthenticationViewModel.authNumber.postValue(s.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            editTextEmail.addTextChangedListener(editTextEmailTextWatcher)
            editTextEmailAuthNumber.addTextChangedListener(editTextEmailAuthNumberTextWatcher)
        }

        with(emailAuthenticationViewModel) {
            email.observe(viewLifecycleOwner) {
                binding.canSendAuthNumber = it.isNotEmpty()
            }

            authNumber.observe(viewLifecycleOwner) {
                binding.canFinishAuth = it.isNotEmpty()
            }

            sentEmailAuth.observe(viewLifecycleOwner) {
                binding.sentAuthNumber = it
            }

            finishedEmailAuth.observe(viewLifecycleOwner) {
                if (it) nextStep()
            }

            showResentEmailAuthNumberDialog.observe(viewLifecycleOwner) {
                showResentEmailAuthNumberDialog()
            }

            showEmailAuthFailedDialog.observe(viewLifecycleOwner) {
                showEmailAuthFailedDialog()
            }
        }
    }

    fun onClickSendAuthNumber(view: View) {
        emailAuthenticationViewModel.sendAuthNumber()
    }

    fun onClickFinishEmailAuth(view: View) {
        emailAuthenticationViewModel.finishEmailAuth()
    }

    fun nextStep() {

    }


    private fun showResentEmailAuthNumberDialog() {
        (activity as ActivityBase).showSimpleDialog(
                title = "재전송 되었습니다.",
                message = "아우누리 메일을 확인해주세요.\n이메일 인증 완료 후 서비스를 이용할 수 있습니다.",
                positiveButtonText = "확인",
                positiveButtonOnClickListener = { (activity as ActivityBase).dismissSimpleDialog() }
        )
    }

    private fun showEmailAuthFailedDialog() {
        (activity as ActivityBase).showSimpleDialog(
                title = "인증 오류",
                message = "아우누리 메일을 확인해주세요.\n이메일 인증 완료 후 서비스를 이용할 수 있습니다.",
                positiveButtonText = "다시 인증하기",
                positiveButtonOnClickListener = {
                    binding.editTextEmailAuthNumber.setText("")
                    (activity as ActivityBase).dismissSimpleDialog()
                }
        )
    }

}