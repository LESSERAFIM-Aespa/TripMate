package kr.sparta.tripmate.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Procedure

/**
 * 작성자 : 김성환
 * 데이터베이스에 액세스하여 Procedure테이블의값들 관리하는 메서드가있습니다.
 * */
@Dao
interface ProcedureDao {
    @Insert
    suspend fun insertProcedures(vararg procedures: Procedure)

    @Update
    suspend fun updateProcedures(vararg procedures: Procedure)

    @Delete
    suspend fun deleteProcedures(vararg procedures: Procedure)

    @Query("SELECT * FROM PROCEDURE")
    fun getAllProceduresToFlow(): Flow<List<Procedure>>

    /**
     * 해당 기본키를 가지고있는 값을 가져옵니다.
     * 반환은 size 가 0이거나 1이므로 isEmpty로 조건문을 확인할것
     * */
    @Query("SELECT * FROM PROCEDURE WHERE NUM = :num")
    suspend fun getAllProceduresWithNum(num: Int): List<Procedure>

    @Query("SELECT * FROM PROCEDURE WHERE NUM = :num")
    fun getAllProceduresWithNumToFlow(num: Int): Flow<List<Procedure>>

    @Query("SELECT * FROM PROCEDURE WHERE CATEGORY_NUM = :categoryNum")
    suspend fun getAllProceduresWithCategoryNum(categoryNum: Int): List<Procedure>

    @Query("SELECT * FROM PROCEDURE WHERE CATEGORY_NUM IN (:categoryNums)")
    suspend fun getAllProceduresWithCategoryNums(categoryNums: List<Int>): List<Procedure>

    @Query("SELECT * FROM PROCEDURE WHERE CATEGORY_NUM IN (:categoryNums) ORDER BY TIME ASC")
    suspend fun getAllProceduresOrderByTimeWithCategoryNums(categoryNums: List<Int>): List<Procedure>
}