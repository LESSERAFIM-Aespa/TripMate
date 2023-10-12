package kr.sparta.tripmate.ui.mypage.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.sparta.tripmate.R
import kr.sparta.tripmate.ui.mypage.board.BoardFragment
import kr.sparta.tripmate.ui.mypage.scrap.ScrapBookmarkFragment

class MyPageTabLayoutAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragments = ArrayList<MyPageTab>()

    init {
        fragments.add(
            MyPageTab(
                fragment=BoardFragment.newInstance(),
                title= R.string.mypage_tab_board,
            ),
        )
        fragments.add(
            MyPageTab(
                fragment = ScrapBookmarkFragment.newInstance(),
                title = R.string.mypage_tab_scrap,
            ),
        )
    }

    fun getTitle(position: Int): Int = fragments[position].title

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position].fragment
}