package `in`.hangang.hangang.ui.home

import `in`.hangang.core.base.fragment.ViewBindingFragment
import `in`.hangang.core.view.appbar.appBarImageButton
import `in`.hangang.hangang.R
import `in`.hangang.hangang.databinding.FragmentHomeBinding
import `in`.hangang.hangang.util.LogUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

class HomeFragment : ViewBindingFragment<FragmentHomeBinding>() {

    override val layoutId = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAppBar()
    }

    private fun initAppBar() {
        with(binding.appBar) {
            addViewInLeft(LayoutInflater.from(activity).inflate(R.layout.hangang_full_logo_small, null))
            addViewInRight(appBarImageButton(R.drawable.ic_search))
            setOnAppBarButtonClickListener(
                    onClickViewInRightContainer = { view, index ->
                        if (index == 0) {
                            LogUtil.d("Search button clicked")
                        }
                    }
            )
        }
    }
}