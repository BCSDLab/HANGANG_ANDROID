package `in`.hangang.core.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class ViewBindingActivity<T : ViewDataBinding>(@LayoutRes private val layoutId: Int) : ActivityBase() {
    protected lateinit var binding : T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}