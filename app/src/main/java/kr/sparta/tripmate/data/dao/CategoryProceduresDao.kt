package kr.sparta.tripmate.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kr.sparta.tripmate.data.model.budget.CategoryProcedures

/**
 * 작성자 : 김성환
 * 데이터베이스에 액세스하여 카테고리테이블의값들 과 연동되는 과정들을 받아오는 메서드가 들어있습니다.
 * */
@Dao
interface CategoryProceduresDao {
    @Transaction
    @Query("SELECT * FROM CATEGORY")
    suspend fun getAllCategoryProcedures(): List<CategoryProcedures>

    @Transaction
    @Query("SELECT * FROM CATEGORY WHERE NUM = :num")
    suspend fun getAllCategoryProceduresWithNum(num: Int): List<CategoryProcedures>

    @Transaction
    @Query("SELECT * FROM CATEGORY WHERE BUDGET_NUM = :budgetNum")
    suspend fun getAllCategoryProceduresWithBudgetNum(budgetNum: Int): List<CategoryProcedures>
}