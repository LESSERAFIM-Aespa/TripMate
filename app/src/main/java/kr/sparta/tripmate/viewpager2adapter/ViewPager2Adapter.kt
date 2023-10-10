package kr.sparta.tripmate.viewpager2adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.sparta.tripmate.fragment.budget.BudgetFragment
import kr.sparta.tripmate.fragment.community.CommunityFragment
import kr.sparta.tripmate.fragment.gourmet.GourmetFragment
import kr.sparta.tripmate.fragment.home.HomeFragment
import kr.sparta.tripmate.fragment.mypage.MyPageFragment

class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int = 5
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> BudgetFragment()

            1 -> CommunityFragment()

            2 -> HomeFragment()

            3 -> GourmetFragment()

            else -> MyPageFragment()
        }
    }
}