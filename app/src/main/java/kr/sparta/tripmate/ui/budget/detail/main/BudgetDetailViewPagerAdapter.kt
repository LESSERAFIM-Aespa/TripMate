package kr.sparta.tripmate.ui.budget.detail.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.sparta.tripmate.R
import kr.sparta.tripmate.ui.budget.BudgetDetailProcedureFragment
import kr.sparta.tripmate.ui.budget.detail.statistics.BudgetDetailStatisticsFragment

class BudgetDetailViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragments = ArrayList<BudgetDetailTab>()

    init {
        fragments.add(
            BudgetDetailTab(
                fragment = BudgetDetailProcedureFragment.newInstance(),
                title = R.string.budget_detail_tab_procedure,
            )
        )
        fragments.add(
            BudgetDetailTab(
                fragment = BudgetDetailStatisticsFragment.newInstance(),
                title = R.string.budget_detail_tab_statistics,
            )
        )
    }

    fun getTitle(position: Int): Int = fragments[position].title

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }

}