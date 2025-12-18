package kr.sparta.tripmate.data.repository.budget

import kr.sparta.tripmate.data.datasource.local.budget.BudgetCategoriesLocalDataSource
import kr.sparta.tripmate.domain.repository.budget.BudgetCategoriesRepository
import javax.inject.Inject

class BudgetCategoriesRepositoryImpl @Inject constructor(
    private val budgetCategoriesDataSource: BudgetCategoriesLocalDataSource,
) : BudgetCategoriesRepository {
    override suspend fun getBudgetCategories(num: Int) =
        budgetCategoriesDataSource.getAllBudgetCategories(num)
}