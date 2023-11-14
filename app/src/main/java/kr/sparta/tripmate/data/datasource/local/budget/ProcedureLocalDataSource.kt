package kr.sparta.tripmate.data.datasource.local.budget

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.data.room.BudgetDatabase

class ProcedureLocalDataSource(context: Context) {
    private val procedureDao = BudgetDatabase.getDatabsae(context).getProcedureDao()

    suspend fun insertProcedures(vararg procedures: Procedure) {
        procedureDao.insertProcedures(*procedures)
    }

    suspend fun updateProcedures(vararg procedures: Procedure) {
        procedureDao.updateProcedures(*procedures)
    }

    suspend fun deleteProcedures(vararg procedures: Procedure) {
        procedureDao.deleteProcedures(*procedures)
    }
    suspend fun getAllProceduresWithNum(num: Int): List<Procedure> =
        procedureDao.getAllProceduresWithNum(num)

    suspend fun getAllProceduresWithCategoryNum(categoryNum: Int): List<Procedure> =
        procedureDao.getAllProceduresWithCategoryNum(categoryNum)

    suspend fun getAllProceduresWithCategoryNums(categoryNums: List<Int>): List<Procedure> =
        procedureDao.getAllProceduresWithCategoryNums(categoryNums)

    suspend fun getAllProceduresOrderByTimeWithCategoryNums(categoryNums: List<Int>): List<Procedure> =
        procedureDao.getAllProceduresOrderByTimeWithCategoryNums(categoryNums)

    fun getAllProceduresToFlow(): Flow<List<Procedure>> = procedureDao.getAllProceduresToFlow()
    fun getAllProceduresWithNumToFlow(num: Int): Flow<List<Procedure>> =
        procedureDao.getAllProceduresWithNumToFlow(num)
}