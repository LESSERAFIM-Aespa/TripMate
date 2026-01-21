package kr.sparta.tripmate.domain.usecase.budgetrepository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository
import javax.inject.Inject

class GetAllBudgetsToFlowWhenBudgetChangedUseCase @Inject constructor(private val repository: BudgetRepository) {
    operator fun invoke(): Flow<List<Budget>> =
        repository.getAllBudgetsToFlowWhenBudgetChanged()
}