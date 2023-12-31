package kr.sparta.tripmate.domain.repository.budget

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Category

interface CategoryRepository {
    suspend fun insertCategories(vararg categories: Category)
    suspend fun updateCategories(vararg categories: Category)
    suspend fun deleteCategories(vararg categories: Category)

    suspend fun getAllCategoriesWithBudgetNum(budgetNum: Int): List<Category>
}