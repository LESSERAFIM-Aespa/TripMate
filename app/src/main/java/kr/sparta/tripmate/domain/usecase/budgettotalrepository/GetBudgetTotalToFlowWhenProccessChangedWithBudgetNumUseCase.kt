package kr.sparta.tripmate.domain.usecase.budgettotalrepository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.budget.BudgetTotalRepository

class GetBudgetTotalToFlowWhenProccessChangedWithBudgetNumUseCase(private val repository: BudgetTotalRepository) {
    operator fun invoke(num: Int): Flow<Triple<Budget, List<Category>, List<Procedure>>> =
        repository.getBudgetTotalToFlowWhenProccessChangedWithBudgetNum(num)
}