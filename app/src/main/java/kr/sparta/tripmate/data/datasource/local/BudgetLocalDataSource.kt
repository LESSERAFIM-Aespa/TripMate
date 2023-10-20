package kr.sparta.tripmate.data.datasource.local

import android.content.Context
import kotlinx.coroutines.flow.flow
import kr.sparta.tripmate.data.room.BudgetDatabase

/**
 * 작성자: 서정한
 * 내용: 가계부 Local DataSource
 * */
class BudgetLocalDataSource {
    /**
     * 최종적으로 해당값(num)을 부모로 가지는 과정들을 가져옴
     * 과정(Procedure)테이블이 바뀌때마다 가져옴
     * */
    fun getAllProceduresToFlowWhenProccessChangedWithBudgetNum(context: Context, num: Int) = flow {
        val database = BudgetDatabase.getDatabsae(context)
        val proceduresDao = database.getProcedureDao()
        val budgetCategoriesDao = database.getBudgetCategoriesDao()

        proceduresDao.getAllProceduresToFlow().collect {
            val list = budgetCategoriesDao.getAllBudgetCategories(num).map {
                it.categories.orEmpty().map { it.num }
            }.reduce { acc, ints -> acc + ints }
            val result = proceduresDao.getAllProceduresOrderByTimeWithCategoryNums(list)
            emit(result)
        }
    }
}