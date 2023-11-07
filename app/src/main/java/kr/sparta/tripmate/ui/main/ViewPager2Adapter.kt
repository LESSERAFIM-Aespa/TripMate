package kr.sparta.tripmate.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.sparta.tripmate.R
import kr.sparta.tripmate.ui.budget.BudgetFragment
import kr.sparta.tripmate.ui.community.main.CommunityFragment
import kr.sparta.tripmate.ui.home.HomeFragment
import kr.sparta.tripmate.ui.mypage.home.MyPageFragment
import kr.sparta.tripmate.ui.scrap.main.ScrapFragment


/**
 * 작성자: 서정한
 * 내용: ViewPager의 Fragment를 관리하기위한 Adapter
 * */
class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    private val fragments = ArrayList<MainTabs>().apply {
        add(
            MainTabs(
                fragment = BudgetFragment.newInstance(),
                title =R.string.main_tab_title_budget,
                icon = R.drawable.budget,
            )
        )
        add(
            MainTabs(
                fragment = CommunityFragment(),
                title =R.string.main_tab_title_community,
                icon = R.drawable.community,
            )
        )
        add(
            MainTabs(
                fragment = HomeFragment.newInstance(),
                title =R.string.main_tab_title_home,
                icon = R.drawable.home,
            )
        )
        add(
            MainTabs(
                fragment = ScrapFragment.newInstance(),
                title =R.string.main_tab_title_blog,
                icon = R.drawable.scrap,
            )
        )
        add(
            MainTabs(
                fragment = MyPageFragment.newInstance(),
                title =R.string.main_tab_title_mypage,
                icon = R.drawable.mypage,
            )
        )
    }

    // HomeFragment의 index를 찾아서 반환해줌
    fun findFragmentTabIndex(name: Int) : Int {
        return when(name) {
            R.string.main_tab_title_budget -> {
                val element = fragments.find { it.title == name }
                fragments.indexOf(element)
            }
            R.string.main_tab_title_community -> {
                val element = fragments.find { it.title == name }
                fragments.indexOf(element)
            }
            R.string.main_tab_title_home -> {
                val element = fragments.find { it.title == name }
                fragments.indexOf(element)
            }
            R.string.main_tab_title_blog -> {
                val element = fragments.find { it.title == name }
                fragments.indexOf(element)
            }
            R.string.main_tab_title_mypage -> {
                val element = fragments.find { it.title == name }
                fragments.indexOf(element)
            }
            else -> 0
        }
    }

    fun getTitme(position : Int): Int = fragments[position].title
    fun getIcon(position: Int): Int = fragments[position].icon

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position].fragment
}