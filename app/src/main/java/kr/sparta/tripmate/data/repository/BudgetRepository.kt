package kr.sparta.tripmate.data.repository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure

interface BudgetRepository {
    suspend fun insertBudgets(vararg budgets: Budget)
    suspend fun updaateBudgets(vararg budgets: Budget)
    suspend fun deleteBugets(vararg budgets: Budget)
    suspend fun insertCategories(vararg categories: Category)
    suspend fun updateCategories(vararg categories: Category)
    suspend fun deleteCategories(vararg categories: Category)
    suspend fun insertProcedures(vararg procedures: Procedure)
    suspend fun updateProcedures(vararg procedures: Procedure)
    suspend fun deleteProcedures(vararg procedures: Procedure)

    /**
     * 가계부(Budget)테이블의 모든값 Flow로 감싸반환,
     * 해당값은 모두 resultMoeny를 가지고있음
     * 가계부(Budget)테이블이 바뀌때마다 가져옴
     * */
    fun getAllBudgetsToFlowWhenBugetsChanged(): Flow<List<Budget>>

    /**
     * 가계부(Budget)테이블의 모든값 Flow로 감싸반환,
     * 해당값은 모두 resultMoeny를 가지고있음
     * 과정(Procedure)테이블이 바뀔때마다 가져옴
     * */
    fun getAllBugetsToFlowWhenProceduresChanged(): Flow<List<Budget>>

    /**
     * 해당키값을 가지는 Budget을 Flow로 감싸 반환
     * 가계부(Budget)테이블이 바뀌때마다 가져옴
     * */
    fun getBugetToFlowWhenBudgetChangedWithNum(num: Int): Flow<Budget>

    /**
     * 해당키값(num)을 부모로 가지는 모든 카테고리들을 가져옴
     * 카테고리(Category)테이블이 바뀌때마다 가져옴
     * */
    fun getCategoriesToFlowWhenCategoriesChanged(num: Int): Flow<List<Category>>

    /**
     * 최종적으로 해당값(num)을 부모로 가지는 과정들을 가져옴
     * 과정(Procedure)테이블이 바뀌때마다 가져옴
     * */
    fun getAllProceduresToFlowWhenProccessChangedWithBudgetNum(num: Int): Flow<List<Procedure>>
}