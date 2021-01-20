package `in`.hangang.hangang.ui.findpassword.adapter

import `in`.hangang.hangang.ui.findpassword.fragment.EmailAuthenticationFragment
import `in`.hangang.hangang.ui.findpassword.fragment.NewPasswordFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.lang.IllegalStateException

class NewPasswordPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val emailAuthenticationFragment = EmailAuthenticationFragment()
    private val newPasswordFragment = NewPasswordFragment()

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> emailAuthenticationFragment
            1 -> newPasswordFragment
            else -> throw IndexOutOfBoundsException()
        }
    }
}