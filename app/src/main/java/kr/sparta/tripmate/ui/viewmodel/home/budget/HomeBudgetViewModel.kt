package kr.sparta.tripmate.ui.viewmodel.home.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetAllBudgetsToFlowWhenBudgetChangedUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetAllBugetsToFlowWhenProceduresChangedUseCase

class HomeBudgetViewModel(
    getAllBudgetsToFlowWhenBudgetChangedUseCase: GetAllBudgetsToFlowWhenBudgetChangedUseCase,
    getAllBugetsToFlowWhenProceduresChangedUseCase:GetAllBugetsToFlowWhenProceduresChangedUseCase,
) : ViewModel() {
    val budgetLiveDataWhenBudgetChanged: LiveData<List<Budget>> =
        getAllBudgetsToFlowWhenBudgetChangedUseCase().asLiveData()
    val budgetLiveDataWhenProcedureChanged: LiveData<List<Budget>> =
        getAllBugetsToFlowWhenProceduresChangedUseCase().asLiveData()
}