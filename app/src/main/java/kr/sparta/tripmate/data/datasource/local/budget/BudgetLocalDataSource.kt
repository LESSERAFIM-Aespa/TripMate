package kr.sparta.tripmate.data.datasource.local.budget

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.dao.BudgetDao
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.room.BudgetDatabase

class BudgetLocalDataSource(context: Context) {
    private val budgetDao: BudgetDao = BudgetDatabase.getDatabsae(context).getBudgetDao()

    suspend fun insertBudgets(vararg budget: Budget) {
        budgetDao.insertBudgets(*budget)
    }

    suspend fun updateBudgets(vararg budget: Budget) {
        budgetDao.updateBudgets(*budget)
    }

    suspend fun delteBudgets(vararg budget: Budget) {
        budgetDao.deleteBudgets(*budget)
    }

    suspend fun deleteAllBudgets() = budgetDao.deleteAllBudgets()

    suspend fun getAllBudgetsOrederByDate(): List<Budget> = budgetDao.getAllBudgetsOrederByDate()
    suspend fun getLastBudget(): List<Budget> = budgetDao.getLastBudget()

    //flow
    fun getAllBudgetsOrederByDateToFlow(): Flow<List<Budget>> =
        budgetDao.getAllBudgetsOrederByDateToFlow()

    fun getAllBudgetsToFlowWithNum(num: Int): Flow<List<Budget>> =
        budgetDao.getAllBudgetsToFlowWithNum(num)
}