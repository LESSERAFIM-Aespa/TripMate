package kr.sparta.tripmate.ui.viewmodel.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository

class BudgetViewModel(repository: BudgetRepository) : ViewModel() {
    val budgetLiveDataWhenBudgetChanged: LiveData<List<Budget>> =
        repository.getAllBudgetsToFlowWhenBugetsChanged().asLiveData()
    val budgetLiveDataWhenProcedureChanged: LiveData<List<Budget>> =
        repository.getAllBugetsToFlowWhenProceduresChanged().asLiveData()
}