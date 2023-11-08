package kr.sparta.tripmate.data.repository.budget

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.sparta.tripmate.data.datasource.local.budget.BudgetCategoriesLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.budget.BudgetTotalRepository

class BudgetTotalRepositoryImpl(
    private val procedureDataSource: ProcedureLocalDataSource,
    private val budgetCategoriesDataSource: BudgetCategoriesLocalDataSource,
) : BudgetTotalRepository {
    override fun getBudgetTotalToFlowWhenProccessChangedWithBudgetNum(num: Int): Flow<Triple<Budget, List<Category>, List<Procedure>>> =
        flow {
            procedureDataSource.getAllProceduresToFlow().collect {
                val triple = budgetCategoriesDataSource.getAllBudgetCategories(num)
                if (triple.isNotEmpty()) {
                    val budgetCategories = triple.first()
                    val budget = budgetCategories.budget
                    val categories = budgetCategories.categories.orEmpty()
                    val procedures =
                        procedureDataSource.getAllProceduresOrderByTimeWithCategoryNums(categories.map { it.num })
                    emit(Triple(budget, categories, procedures))
                }
            }
        }
}