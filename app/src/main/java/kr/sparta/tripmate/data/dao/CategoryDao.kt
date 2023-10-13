package kr.sparta.tripmate.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Category

/**
 * 작성자 : 김성환
 * 데이터베이스에 액세스하여 Category테이블의값들 관리하는 메서드가있습니다.
 * */
@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategories(vararg category: Category)

    @Update
    suspend fun updateCategories(vararg category: Category)

    @Delete
    suspend fun deleteCategories(vararg category: Category)

    @Query("SELECT * FROM CATEGORY")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM CATEGORY WHERE NUM = :num")
    suspend fun getAllCategoriesWithNum(num: Int): List<Category>

    @Query("SELECT * FROM CATEGORY WHERE BUDGET_NUM = :budgetNum")
    suspend fun getAllCategoriesWithBudgetNum(budgetNum: Int): List<Category>

    @Query("SELECT * FROM CATEGORY WHERE BUDGET_NUM = :budgetNum")
    fun getAllCategoriesToFlowWithBudgetNum(budgetNum: Int): Flow<List<Category>>
}