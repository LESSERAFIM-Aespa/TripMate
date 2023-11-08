package kr.sparta.tripmate.domain.repository.budget

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Budget

interface BudgetRepository {
    suspend fun insertBudgets(vararg budgets: Budget)
    suspend fun updateBudgets(vararg budgets: Budget)
    suspend fun deleteBugets(vararg budgets: Budget)

    fun getAllBudgetsToFlowWhenBugetsChanged(): Flow<List<Budget>>
    fun getAllBugetsToFlowWhenProceduresChanged(): Flow<List<Budget>>
    fun getBugetToFlowWhenBudgetChangedWithNum(num: Int): Flow<Budget>
    suspend fun getLastBudget(): List<Budget>
}