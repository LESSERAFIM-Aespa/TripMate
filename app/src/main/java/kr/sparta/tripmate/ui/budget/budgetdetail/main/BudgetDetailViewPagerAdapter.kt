package kr.sparta.tripmate.ui.budget.budgetdetail.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.sparta.tripmate.R
import kr.sparta.tripmate.ui.budget.budgetdetail.procedure.BudgetDetailProcedureFragment
import kr.sparta.tripmate.ui.budget.budgetdetail.statistics.BudgetDetailStatisticsFragment

class BudgetDetailViewPagerAdapter(budgetNum: Int, fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragments = ArrayList<BudgetDetailTab>()

    init {
        fragments.add(
            BudgetDetailTab(
                fragment = BudgetDetailProcedureFragment.newInstance(budgetNum),
                title = R.string.budget_detail_tab_procedure,
            )
        )
        fragments.add(
            BudgetDetailTab(
                fragment = BudgetDetailStatisticsFragment.newInstance(budgetNum),
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