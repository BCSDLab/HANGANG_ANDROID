package `in`.hangang.hangang.ui.changepassword.adapter

import `in`.hangang.hangang.ui.changepassword.fragment.EmailAuthenticationFragment
import `in`.hangang.hangang.ui.changepassword.fragment.ChangePasswordFragment
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChangePasswordPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val emailAuthenticationFragment = EmailAuthenticationFragment()
    private val newPasswordFragment = ChangePasswordFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> emailAuthenticationFragment
            1 -> newPasswordFragment
            else -> throw IndexOutOfBoundsException()
        }
    }
}