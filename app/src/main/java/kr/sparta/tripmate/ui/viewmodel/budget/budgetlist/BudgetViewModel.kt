package kr.sparta.tripmate.ui.viewmodel.budget.budgetlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetAllBudgetsToFlowWhenBudgetChangedUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetAllBudgetsToFlowWhenProcedureChangedUseCase
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    getAllBudgetsToFlowWhenBudgetChangedUseCase: GetAllBudgetsToFlowWhenBudgetChangedUseCase,
    getAllBudgetsToFlowWhenProcedureChangedUseCase: GetAllBudgetsToFlowWhenProcedureChangedUseCase,
) : ViewModel() {
    val budgetLiveDataWhenBudgetChanged: LiveData<List<Budget>> =
        getAllBudgetsToFlowWhenBudgetChangedUseCase().asLiveData()
    val budgetLiveDataWhenProcedureChanged: LiveData<List<Budget>> =
        getAllBudgetsToFlowWhenProcedureChangedUseCase().asLiveData()
}