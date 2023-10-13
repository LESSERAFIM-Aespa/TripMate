package kr.sparta.tripmate.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kr.sparta.tripmate.data.model.budget.BudgetCategories

/**
 * 작성자 : 김성환
 * 데이터베이스에 액세스하여 BUDGET테이블의값들 과 연동되는 카테고리들을 받아오는 메서드가 들어있습니다.
 * */
@Dao
interface BudgetCategoriesDao {
    @Transaction
    @Query("SELECT * FROM BUDGET")
    suspend fun getAllBudgetCategories(): List<BudgetCategories>

    @Transaction
    @Query("SELECT * FROM BUDGET WHERE NUM = :num")
    suspend fun getAllBudgetCategories(num: Int): List<BudgetCategories>
}