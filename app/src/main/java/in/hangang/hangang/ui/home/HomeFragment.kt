package `in`.hangang.hangang.ui.home

import `in`.hangang.core.view.appbar.HangangAppBarClickListener
import `in`.hangang.core.view.appbar.HangangProgressAppBar
import `in`.hangang.hangang.LogUtil
import `in`.hangang.hangang.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appBar = view.findViewById<HangangProgressAppBar>(R.id.app_bar)
        appBar.hangangAppBarClickListener = object : HangangAppBarClickListener {
            override fun onBackButtonClick(v: View) {
                LogUtil.d("back button clicked")
                activity?.finish()
            }

            override fun onRightButtonClick(v: View) {
                LogUtil.d("Right button clicked")
            }
        }
        var progress = 0
        Thread {
            while(progress <= appBar.max) {
                progress += 1
                activity?.runOnUiThread {
                    appBar.progress = progress
                }
                Thread.sleep(100)
            }

        }.start()
    }
}