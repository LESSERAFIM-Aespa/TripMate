package kr.sparta.tripmate.ui.viewmodel.budget.detail.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.budget.BudgetLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.CategoryProceduresLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.repository.budget.BudgetRepositoryImpl
import kr.sparta.tripmate.domain.usecase.budgetrepository.DeleteBudgetsUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetBudgetToFlowWhenBudgetChangedWithNumUseCase
import kr.sparta.tripmate.util.TripMateApp

class BudgetDetailFactory(private val budgetNum: Int) : ViewModelProvider.Factory {
    private val budgetRepository by lazy {
        BudgetRepositoryImpl(
            BudgetLocalDataSource(TripMateApp.getApp().applicationContext),
            ProcedureLocalDataSource(TripMateApp.getApp().applicationContext),
            CategoryProceduresLocalDataSource(TripMateApp.getApp().applicationContext),
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetDetailViewModel::class.java)) {
            return BudgetDetailViewModel(
                DeleteBudgetsUseCase(budgetRepository),
                GetBudgetToFlowWhenBudgetChangedWithNumUseCase(budgetRepository),
                budgetNum
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}