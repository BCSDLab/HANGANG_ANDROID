package `in`.hangang.hangang.ui.home
import `in`.hangang.core.view.appbar.ProgressAppBar
import `in`.hangang.core.view.appbar.appBarTextButton
import `in`.hangang.core.view.appbar.interfaces.OnAppBarButtonClickListener
import `in`.hangang.hangang.R
import `in`.hangang.hangang.ui.signup.SignUpActivity
import `in`.hangang.hangang.ui.signup.SignUpDocumentActivity
import `in`.hangang.hangang.ui.signup.SignUpEmailActivity
import `in`.hangang.hangang.ui.signup.SignUpMajorActivity
import `in`.hangang.hangang.util.LogUtil
import android.content.Intent
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
        val appBar = view.findViewById<ProgressAppBar>(R.id.app_bar)

        activity?.let {
            appBar.addViewInRight(it.appBarTextButton("우1"))
            appBar.addViewInRight(it.appBarTextButton("우2"))
            appBar.addViewInLeft(it.appBarTextButton("좌1"))
            appBar.addViewInLeft(it.appBarTextButton("좌2"))
            appBar.removeViewInLeft(1)
        }

        appBar.onAppBarButtonButtonClickListener = object : OnAppBarButtonClickListener {
            override fun onClickViewInLeftContainer(view: View, index: Int) {
                LogUtil.d("Clicked left button #$index")
            }

            override fun onClickViewInRightContainer(view: View, index: Int) {
                LogUtil.d("Clicked right button #$index")
            }
        }
    }
}