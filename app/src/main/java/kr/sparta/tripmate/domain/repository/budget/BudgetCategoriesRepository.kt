package kr.sparta.tripmate.domain.repository.budget

import kr.sparta.tripmate.data.model.budget.BudgetCategories

interface BudgetCategoriesRepository {
    suspend fun getBudgetCategories(num: Int) : List<BudgetCategories>
}