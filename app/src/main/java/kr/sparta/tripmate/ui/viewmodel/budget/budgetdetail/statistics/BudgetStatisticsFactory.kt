package kr.sparta.tripmate.ui.viewmodel.budget.budgetdetail.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.budget.BudgetCategoriesLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.repository.budget.BudgetTotalRepositoryImpl
import kr.sparta.tripmate.domain.usecase.budgettotalrepository.GetBudgetTotalToFlowWhenProccessChangedWithBudgetNumUseCase
import kr.sparta.tripmate.util.TripMateApp

class BudgetStatisticsFactory(val budgetNum: Int) : ViewModelProvider.Factory {
    private val budgetTotalRepository by lazy {
        BudgetTotalRepositoryImpl(
            ProcedureLocalDataSource(TripMateApp.getApp().applicationContext),
            BudgetCategoriesLocalDataSource(TripMateApp.getApp().applicationContext)
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetStatisticsViewModel::class.java)) {
            return BudgetStatisticsViewModel(
                GetBudgetTotalToFlowWhenProccessChangedWithBudgetNumUseCase(budgetTotalRepository),
                budgetNum
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}