package kr.sparta.tripmate.ui.viewmodel.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.domain.repository.BudgetRepository
import kr.sparta.tripmate.ui.budget.BudgetContentType
import kr.sparta.tripmate.ui.budget.ProcedureContentType


class ProcedureContentViewModelFactory(
    private val repository: BudgetRepository,
    private val entryType: ProcedureContentType,
    private val budgetNum: Int,
    private val procedureNum: Int = 0,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProcedureContentViewModel::class.java)) {
            return ProcedureContentViewModel(repository,entryType,budgetNum, procedureNum) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}