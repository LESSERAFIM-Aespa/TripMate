package kr.sparta.tripmate.ui.viewmodel.home.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.budget.BudgetLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.CategoryProceduresLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.repository.budget.BudgetRepositoryImpl
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetAllBudgetsToFlowWhenBugetsChangedUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetAllBugetsToFlowWhenProceduresChangedUseCase
import kr.sparta.tripmate.util.TripMateApp

class HomeBudgetFactory : ViewModelProvider.Factory {
    private val budgetRepository by lazy {
        BudgetRepositoryImpl(
            BudgetLocalDataSource(TripMateApp.getApp().applicationContext),
            ProcedureLocalDataSource(TripMateApp.getApp().applicationContext),
            CategoryProceduresLocalDataSource(TripMateApp.getApp().applicationContext),
        )
    }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeBudgetViewModel::class.java)) {
            return HomeBudgetViewModel(
                GetAllBudgetsToFlowWhenBugetsChangedUseCase(budgetRepository),
                GetAllBugetsToFlowWhenProceduresChangedUseCase(budgetRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}