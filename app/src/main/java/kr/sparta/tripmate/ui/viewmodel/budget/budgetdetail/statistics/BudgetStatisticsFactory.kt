package kr.sparta.tripmate.ui.viewmodel.budget.budgetdetail.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.budget.BudgetCategoriesLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.BudgetLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.CategoryProceduresLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.repository.budget.BudgetRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.BudgetTotalRepositoryImpl
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetBudgetToFlowWhenBudgetChangedWithNumUseCase
import kr.sparta.tripmate.domain.usecase.budgettotalrepository.GetBudgetTotalToFlowWhenProccessChangedWithBudgetNumUseCase
import kr.sparta.tripmate.di.TripMateApp

class BudgetStatisticsFactory(val budgetNum: Int) : ViewModelProvider.Factory {
    private val budgetRepository by lazy {
        BudgetRepositoryImpl(
            BudgetLocalDataSource(TripMateApp.getApp().applicationContext),
            ProcedureLocalDataSource(TripMateApp.getApp().applicationContext),
            CategoryProceduresLocalDataSource(TripMateApp.getApp().applicationContext),
        )
    }
    private val budgetTotalRepository by lazy {
        BudgetTotalRepositoryImpl(
            ProcedureLocalDataSource(TripMateApp.getApp().applicationContext),
            BudgetCategoriesLocalDataSource(TripMateApp.getApp().applicationContext)
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetStatisticsViewModel::class.java)) {
            return BudgetStatisticsViewModel(
                GetBudgetToFlowWhenBudgetChangedWithNumUseCase(budgetRepository),
                GetBudgetTotalToFlowWhenProccessChangedWithBudgetNumUseCase(budgetTotalRepository),
                budgetNum
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}