package kr.sparta.tripmate.domain.repository.budget

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Procedure

interface ProcedureRepository {
    suspend fun insertProcedures(vararg procedures: Procedure)
    suspend fun updateProcedures(vararg procedures: Procedure)
    suspend fun deleteProcedures(vararg procedures: Procedure)

    suspend fun getProceduresWithNum(num: Int): List<Procedure>
    suspend fun getProcedouresWithCategoryNum(num: Int): List<Procedure>
    suspend fun getAllProceuduresWithCategoryNums(nums: List<Int>): List<Procedure>
    fun getProcedureToFlowWithNum(num: Int): Flow<List<Procedure>>
}