package kr.sparta.tripmate.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.sparta.tripmate.ui.budget.BudgetFragment
import kr.sparta.tripmate.ui.community.main.CommunityFragment
import kr.sparta.tripmate.ui.scrap.ScrapFragment
import kr.sparta.tripmate.ui.home.HomeFragment
import kr.sparta.tripmate.ui.mypage.home.MyPageFragment

class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int = 5
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> BudgetFragment()

            1 -> CommunityFragment()

            2 -> HomeFragment()

            3 -> ScrapFragment()

            else -> MyPageFragment.newInstance()
        }
    }
}