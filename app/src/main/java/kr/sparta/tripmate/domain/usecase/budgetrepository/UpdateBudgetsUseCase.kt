package kr.sparta.tripmate.domain.usecase.budgetrepository

import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository
import javax.inject.Inject

class UpdateBudgetsUseCase @Inject constructor(private val repository: BudgetRepository) {
    suspend operator fun invoke(vararg budgets: Budget) {
        repository.updateBudgets(*budgets)
    }
}