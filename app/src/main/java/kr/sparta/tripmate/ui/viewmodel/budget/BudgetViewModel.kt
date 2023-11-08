package kr.sparta.tripmate.ui.viewmodel.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetAllBudgetsToFlowWhenBudgetChangedUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetAllBudgetsToFlowWhenProcedureChangedUseCase

class BudgetViewModel(
    getAllBudgetsToFlowWhenBudgetChangedUseCase: GetAllBudgetsToFlowWhenBudgetChangedUseCase,
    getAllBudgetsToFlowWhenProcedureChangedUseCase: GetAllBudgetsToFlowWhenProcedureChangedUseCase,
) : ViewModel() {
    val budgetLiveDataWhenBudgetChanged: LiveData<List<Budget>> =
        getAllBudgetsToFlowWhenBudgetChangedUseCase().asLiveData()
    val budgetLiveDataWhenProcedureChanged: LiveData<List<Budget>> =
        getAllBudgetsToFlowWhenProcedureChangedUseCase().asLiveData()
}