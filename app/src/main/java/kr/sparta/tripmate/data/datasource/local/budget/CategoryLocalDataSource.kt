package kr.sparta.tripmate.data.datasource.local.budget

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.dao.CategoryDao
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.room.BudgetDatabase

class CategoryLocalDataSource(context: Context) {
    private val categoryDao: CategoryDao = BudgetDatabase.getDatabsae(context).getCategoryDao()

    suspend fun insertCategories(vararg category: Category) {
        categoryDao.insertCategories(*category)
    }

    suspend fun updateCategories(vararg category: Category) {
        categoryDao.updateCategories(*category)
    }

    suspend fun deleteCategories(vararg category: Category) {
        categoryDao.deleteCategories(*category)
    }

    suspend fun getAllCategoriesWithBudgetNum(budgetNum: Int): List<Category> =
        categoryDao.getAllCategoriesWithBudgetNum(budgetNum)

    //flow
    fun getAllCategoriesToFlowWithBudgetNum(budgetNum: Int): Flow<List<Category>> =
        categoryDao.getAllCategoriesToFlowWithBudgetNum(budgetNum)
}