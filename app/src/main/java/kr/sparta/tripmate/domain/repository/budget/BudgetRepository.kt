package kr.sparta.tripmate.domain.repository.budget

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Budget

interface BudgetRepository {
    suspend fun insertBudgets(vararg budgets: Budget)
    suspend fun updateBudgets(vararg budgets: Budget)
    suspend fun deleteBugets(vararg budgets: Budget)

    fun getAllBudgetsToFlowWhenBudgetChanged(): Flow<List<Budget>>
    fun getAllBudgetsToFlowWhenProcedureChanged(): Flow<List<Budget>>
    fun getBudgetToFlowWhenBudgetChangedWithNum(num: Int): Flow<Budget>
    suspend fun getLastBudget(): List<Budget>
}