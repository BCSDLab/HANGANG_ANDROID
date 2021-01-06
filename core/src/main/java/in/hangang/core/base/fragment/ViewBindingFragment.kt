package `in`.hangang.core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class ViewBindingFragment<T : ViewDataBinding>(@LayoutRes private val layoutId: Int) : FragmentBase() {

    protected lateinit var binding : T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}