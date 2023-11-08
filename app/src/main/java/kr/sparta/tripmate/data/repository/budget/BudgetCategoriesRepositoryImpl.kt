package kr.sparta.tripmate.data.repository.budget

import kr.sparta.tripmate.data.datasource.local.budget.BudgetCategoriesLocalDataSource
import kr.sparta.tripmate.domain.repository.budget.BudgetCategoriesRepository

class BudgetCategoriesRepositoryImpl(
    private val budgetCategoriesDataSource: BudgetCategoriesLocalDataSource,
) : BudgetCategoriesRepository {
    override suspend fun getBudgetCategories(num: Int) =
        budgetCategoriesDataSource.getAllBudgetCategories(num)
}