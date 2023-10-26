package kr.sparta.tripmate.ui.userprofile.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.sparta.tripmate.R
import kr.sparta.tripmate.ui.userprofile.board.UserProfileBoardFragment

class UserProfileTabLayoutAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragments = ArrayList<UserProfileTab>()

    init {
        fragments.add(
            UserProfileTab(
                fragment = UserProfileBoardFragment.newInstance(),
                title = R.string.user_profile_tab_title,
            ),
        )
    }

    fun getTitle(position: Int): Int = fragments[position].title

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position].fragment
}