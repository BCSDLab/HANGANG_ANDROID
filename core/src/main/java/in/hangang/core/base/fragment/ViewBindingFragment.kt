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

abstract class ViewBindingFragment<T : ViewDataBinding> : FragmentBase() {
    @get:LayoutRes
    abstract val layoutId: Int
    private lateinit var _binding: T
    val binding: T
        get() = _binding

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
        binding.unbind()
        hideProgressDialog()
        super.onDestroy()
    }
}