package kr.sparta.tripmate.data.repository.budget

import kotlinx.coroutines.flow.flow
import kr.sparta.tripmate.data.datasource.local.budget.BudgetLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.CategoryProceduresLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository

class BudgetRepositoryImpl(
    private val budgetDataSource: BudgetLocalDataSource,
    private val procedureDataSource: ProcedureLocalDataSource,
    private val categoryProceduresDataSource: CategoryProceduresLocalDataSource,
) : BudgetRepository {
    override suspend fun insertBudgets(vararg budgets: Budget) {
        budgetDataSource.insertBudgets(*budgets)
    }

    override suspend fun updateBudgets(vararg budgets: Budget) {
        budgetDataSource.updateBudgets(*budgets)
    }

    override suspend fun deleteBugets(vararg budgets: Budget) {
        budgetDataSource.delteBudgets(*budgets)
    }

    override fun getAllBudgetsToFlowWhenBudgetChanged() = flow {
        budgetDataSource.getAllBudgetsOrederByDateToFlow().collect { budgets ->
            val list = budgets.map { budget ->
                val categoryProceduresList =
                    categoryProceduresDataSource.getAllCategoryProceduresWithBudgetNum(budget.num)
                val reduce = categoryProceduresList.sumOf { categoryProcedures ->
                    categoryProcedures.procedures.orEmpty().sumOf { it.money }
                }
                budget.resultMoeny = budget.money - reduce
                budget
            }
            emit(list)
        }
    }

    override fun getAllBugetsToFlowWhenProceduresChanged() = flow {
        procedureDataSource.getAllProceduresToFlow().collect {
            val list = budgetDataSource.getAllBudgetsOrederByDate().map { budget ->
                val categoryProceduresList =
                    categoryProceduresDataSource.getAllCategoryProceduresWithBudgetNum(budget.num)
                val reduce = categoryProceduresList.sumOf { categoryProcedures ->
                    categoryProcedures.procedures.orEmpty().sumOf { it.money }
                }
                budget.resultMoeny = budget.money - reduce
                budget
            }
            emit(list)
        }
    }

    override fun getBugetToFlowWhenBudgetChangedWithNum(num: Int) = flow {
        budgetDataSource.getAllBudgetsToFlowWithNum(num).collect {
            if (it.isNotEmpty()) emit(it.first())
        }
    }

    override suspend fun getLastBudget(): List<Budget> = budgetDataSource.getLastBudget()
}