package kr.sparta.tripmate.data.datasource.local.budget

import android.content.Context
import kr.sparta.tripmate.data.dao.CategoryProceduresDao
import kr.sparta.tripmate.data.model.budget.CategoryProcedures
import kr.sparta.tripmate.data.room.BudgetDatabase

class CategoryProceduresLocalDataSource(context: Context) {
    private val categoryProceduresDao: CategoryProceduresDao =
        BudgetDatabase.getDatabsae(context).getCategoryProceduresDao()

    suspend fun getAllCategoryProceduresWithBudgetNum(budgetNum: Int): List<CategoryProcedures> =
        categoryProceduresDao.getAllCategoryProceduresWithBudgetNum(budgetNum)
}