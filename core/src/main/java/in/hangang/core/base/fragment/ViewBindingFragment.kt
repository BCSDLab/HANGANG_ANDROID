package `in`.hangang.core.base.fragment

import `in`.hangang.core.R
import `in`.hangang.core.progressdialog.IProgressDialog
import `in`.hangang.core.progressdialog.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class ViewBindingFragment<T : ViewDataBinding> : FragmentBase(), IProgressDialog {
    @get:LayoutRes
    abstract val layoutId: Int
    private lateinit var _binding: T
    val binding: T
        get() = _binding
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        _binding.lifecycleOwner = this
        return _binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    override fun showProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = ProgressDialog(requireContext(), getString(R.string.loading))
        progressDialog?.show()
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}