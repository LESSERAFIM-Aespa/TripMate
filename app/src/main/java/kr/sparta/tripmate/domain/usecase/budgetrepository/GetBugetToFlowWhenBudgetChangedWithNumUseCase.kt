package kr.sparta.tripmate.domain.usecase.budgetrepository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository

class GetBugetToFlowWhenBudgetChangedWithNumUseCase(private val repository: BudgetRepository) {
    operator fun invoke(num: Int): Flow<Budget> =
        repository.getBugetToFlowWhenBudgetChangedWithNum(num)
}