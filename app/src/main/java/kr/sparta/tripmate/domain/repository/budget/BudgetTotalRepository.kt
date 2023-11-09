package kr.sparta.tripmate.domain.repository.budget

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure

interface BudgetTotalRepository {
    fun getBudgetTotalToFlowWhenProccessChangedWithBudgetNum(num: Int): Flow<Triple<Budget, List<Category>, List<Procedure>>>
}