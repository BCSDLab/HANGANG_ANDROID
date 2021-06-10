package `in`.hangang.core.base.fragment

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
    private var _binding: T? = null
    val binding: T
        get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        _binding!!.lifecycleOwner = this
        return _binding!!.root
    }

    override fun onDestroyView() {
        _binding?.unbind()
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        hideProgressDialog()
        super.onDestroy()
    }
}