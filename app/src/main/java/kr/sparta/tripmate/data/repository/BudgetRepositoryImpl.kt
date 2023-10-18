package kr.sparta.tripmate.data.repository

import android.content.Context
import kotlinx.coroutines.flow.flow
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.data.room.BudgetDatabase
import kr.sparta.tripmate.domain.repository.BudgetRepository

class BudgetRepositoryImpl(context: Context) : BudgetRepository {
    private val database = BudgetDatabase.getDatabsae(context)

    private val budgetDao = database.getBudgetDao()
    private val categoryDao = database.getCategoryDao()
    private val proceduresDao = database.getProcedureDao()
    private val budgetCategoriesDao = database.getBudgetCategoriesDao()
    private val categoryProceduresDao = database.getCategoryProceduresDao()

    override suspend fun insertBudgets(vararg budgets: Budget) {
        budgetDao.insertBudgets(*budgets)
    }

    override suspend fun updateBudgets(vararg budgets: Budget) {
        budgetDao.updateBudgets(*budgets)
    }

    override suspend fun deleteBugets(vararg budgets: Budget) {
        budgetDao.deleteBudgets(*budgets)
    }

    override suspend fun insertCategories(vararg categories: Category) {
        categoryDao.insertCategories(*categories)
    }

    override suspend fun updateCategories(vararg categories: Category) {
        categoryDao.updateCategories(*categories)
    }

    override suspend fun deleteCategories(vararg categories: Category) {
        categoryDao.deleteCategories(*categories)
    }

    override suspend fun insertProcedures(vararg procedures: Procedure) {
        proceduresDao.insertProcedures(*procedures)
    }

    override suspend fun updateProcedures(vararg procedures: Procedure) {
        proceduresDao.updateProcedures(*procedures)
    }

    override suspend fun deleteProcedures(vararg procedures: Procedure) {
        proceduresDao.deleteProcedures(*procedures)
    }

    /**
     * 가계부(Budget)테이블의 모든값 Flow로 감싸반환,
     * 해당값은 모두 resultMoeny를 가지고있음
     * 가계부(Budget)테이블이 바뀌때마다 가져옴
     * */
    override fun getAllBudgetsToFlowWhenBugetsChanged() = flow {
        budgetDao.getAllBudgetsOrederByDateToFlow().collect {
            val list = it.map {
                val categoryProceduresList =
                    categoryProceduresDao.getAllCategoryProceduresWithBudgetNum(it.num)
                val reduce = categoryProceduresList.sumOf {
                    it.procedures.orEmpty().sumOf {
                        it.money
                    }
                }
                it.resultMoeny = it.money - reduce
                it
            }
            emit(list)
        }
    }

    /**
     * 가계부(Budget)테이블의 모든값 Flow로 감싸반환,
     * 해당값은 모두 resultMoeny를 가지고있음
     * 과정(Procedure)테이블이 바뀔때마다 가져옴
     * */
    override fun getAllBugetsToFlowWhenProceduresChanged() = flow {
        proceduresDao.getAllProceduresToFlow().collect {
            val list = budgetDao.getAllBudgetsOrederByDate().map {
                val categoryProceduresList =
                    categoryProceduresDao.getAllCategoryProceduresWithBudgetNum(it.num)
                val reduce = categoryProceduresList.sumOf {
                    it.procedures.orEmpty().sumOf { it.money }
                }
                it.resultMoeny = it.money - reduce
                it
            }
            emit(list)
        }
    }

    /**
     * 해당키값을 가지는 Budget을 Flow로 감싸 반환
     * 가계부(Budget)테이블이 바뀌때마다 가져옴
     * */
    override fun getBugetToFlowWhenBudgetChangedWithNum(num: Int) = flow {
        budgetDao.getAllBudgetsToFlowWithNum(num).collect {
            if (it.isNotEmpty()) emit(it.first())
        }
    }

    /**
     * 해당키값(num)을 부모로 가지는 모든 카테고리들을 가져옴
     * 카테고리(Category)테이블이 바뀌때마다 가져옴
     * */
    override fun getCategoriesToFlowWhenCategoriesChanged(num: Int) = flow {
        categoryDao.getAllCategoriesToFlowWithBudgetNum(num).collect {
            emit(it)
        }
    }

    /**
     * 최종적으로 해당값(num)을 부모로 가지는 과정들을 가져옴
     * 과정(Procedure)테이블이 바뀌때마다 가져옴
     * */
    override fun getAllProceduresToFlowWhenProccessChangedWithBudgetNum(num: Int) = flow {
        proceduresDao.getAllProceduresToFlow().collect {
            val list = budgetCategoriesDao.getAllBudgetCategories(num).map {
                it.categories.orEmpty().map { it.num }
            }.reduce { acc, ints -> acc + ints }
            val result = proceduresDao.getAllProceduresOrderByTimeWithCategoryNums(list)
            emit(result)
        }
    }

    override suspend fun getBudgetCategories(num: Int) = budgetCategoriesDao.getAllBudgetCategories(num)
    override suspend fun getLastBudget(): List<Budget> = budgetDao.getLastBudget()
    override suspend fun getProceduresWithNum(num: Int): List<Procedure> = proceduresDao.getAllProceduresWithNum(num)
}