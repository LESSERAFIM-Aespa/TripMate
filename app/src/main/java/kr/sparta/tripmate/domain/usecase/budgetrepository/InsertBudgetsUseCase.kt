package kr.sparta.tripmate.domain.usecase.budgetrepository

import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository

class InsertBudgetsUseCase(private val repository: BudgetRepository) {
    suspend operator fun invoke(vararg budgets: Budget) {
        repository.insertBudgets(*budgets)
    }
}