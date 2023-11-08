package kr.sparta.tripmate.data.repository.budget

import kr.sparta.tripmate.data.datasource.local.budget.CategoryLocalDataSource
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.domain.repository.budget.CategoryRepository

class CategoryRepositoryImpl(
    private val categoryDataSource: CategoryLocalDataSource,
) : CategoryRepository {
    override suspend fun insertCategories(vararg categories: Category) {
        categoryDataSource.insertCategories(*categories)
    }

    override suspend fun updateCategories(vararg categories: Category) {
        categoryDataSource.updateCategories(*categories)
    }

    override suspend fun deleteCategories(vararg categories: Category) {
        categoryDataSource.deleteCategories(*categories)
    }

    override suspend fun getAllCategoriesWithBudgetNum(budgetNum: Int): List<Category> =
        categoryDataSource.getAllCategoriesWithBudgetNum(budgetNum)
}