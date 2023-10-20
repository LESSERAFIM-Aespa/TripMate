package kr.sparta.tripmate.ui.viewmodel.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.domain.repository.BudgetRepository

class ProcedureDetailViewModelFactory(
    private val repository: BudgetRepository,
    private val budgetNum: Int,
    private val procedureNum: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProcedureDetailViewModel::class.java)) {
            return ProcedureDetailViewModel(repository,budgetNum, procedureNum) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}