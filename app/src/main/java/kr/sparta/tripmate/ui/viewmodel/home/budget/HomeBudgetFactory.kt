package kr.sparta.tripmate.ui.viewmodel.home.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository
import kr.sparta.tripmate.ui.viewmodel.budget.BudgetViewModel

class HomeBudgetFactory(
    private val repository: BudgetRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeBudgetViewModel::class.java)) {
            return HomeBudgetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}