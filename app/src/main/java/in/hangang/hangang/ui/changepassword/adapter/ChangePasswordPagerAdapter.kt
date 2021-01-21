package `in`.hangang.hangang.ui.changepassword.adapter

import `in`.hangang.hangang.ui.changepassword.fragment.EmailAuthenticationFragment
import `in`.hangang.hangang.ui.changepassword.fragment.ChangePasswordFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ChangePasswordPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val emailAuthenticationFragment = EmailAuthenticationFragment()
    private val newPasswordFragment = ChangePasswordFragment()

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> emailAuthenticationFragment
            1 -> newPasswordFragment
            else -> throw IndexOutOfBoundsException()
        }
    }
}