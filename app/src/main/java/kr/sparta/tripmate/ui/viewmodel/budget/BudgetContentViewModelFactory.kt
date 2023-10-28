package kr.sparta.tripmate.ui.viewmodel.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository
import kr.sparta.tripmate.ui.budget.BudgetContentType


class BudgetContentViewModelFactory(
    private val entryType: BudgetContentType,
    private val repository: BudgetRepository,
    private val budgetNum: Int = 0,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetContentViewModel::class.java)) {
            return BudgetContentViewModel(entryType, repository, budgetNum) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}