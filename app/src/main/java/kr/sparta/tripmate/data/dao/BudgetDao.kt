package kr.sparta.tripmate.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Budget

/**
 * 작성자 : 김성환
 * 데이터베이스에 액세스하여 BUDGET테이블의값들 관리하는 메서드가있습니다.
 * */
@Dao
interface BudgetDao {
    @Insert
    suspend fun insertBudgets(vararg budget: Budget)

    @Update
    suspend fun updateBudgets(vararg budget: Budget)

    @Delete
    suspend fun deleteBudgets(vararg budget: Budget)

    @Query("SELECT * FROM BUDGET")
    suspend fun getAllBudgets(): List<Budget>

    @Query("SELECT * FROM BUDGET ORDER BY START_DATE ASC , END_DATE ASC")
    suspend fun getAllBudgetsOrederByDate(): List<Budget>

    @Query("SELECT * FROM BUDGET ORDER BY START_DATE ASC , END_DATE ASC")
    fun getAllBudgetsOrederByDateToFlow(): Flow<List<Budget>>

    /**
     * 해당 기본키를 가지고있는 값을 가져옵니다.
     * 반환은 size 가 0이거나 1이므로 isEmpty로 조건문을 확인할것
     * */
    @Query("SELECT * FROM BUDGET WHERE NUM =:num")
    suspend fun getAllBudgetsWithNum(num: Int): List<Budget>

    @Query("SELECT * FROM BUDGET WHERE NUM =:num")
    fun getAllBudgetsToFlowWithNum(num: Int): Flow<List<Budget>>

    @Query("SELECT * FROM BUDGET ORDER BY NUM LIMIT 1")
    suspend fun getLastBudget(): List<Budget>
}