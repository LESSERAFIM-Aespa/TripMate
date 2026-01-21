package kr.sparta.tripmate.data.datasource.local.budget

import android.content.Context
import kr.sparta.tripmate.data.dao.BudgetCategoriesDao
import kr.sparta.tripmate.data.model.budget.BudgetCategories
import kr.sparta.tripmate.data.datasource.local.BudgetDatabase

class BudgetCategoriesLocalDataSource(context: Context) {
    private val budgetCategoriesDao: BudgetCategoriesDao =
        BudgetDatabase.getDatabsae(context).getBudgetCategoriesDao()

    suspend fun getAllBudgetCategories(num: Int): List<BudgetCategories> =
        budgetCategoriesDao.getAllBudgetCategories(num)
}