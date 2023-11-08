package kr.sparta.tripmate.data.repository.budget

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.datasource.local.budget.BudgetCategoriesLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.budget.ProcedureRepository

class ProcedureRepositoryImpl(
    private val procedureDataSource: ProcedureLocalDataSource,
) : ProcedureRepository {
    override suspend fun insertProcedures(vararg procedures: Procedure) {
        procedureDataSource.insertProcedures(*procedures)
    }

    override suspend fun updateProcedures(vararg procedures: Procedure) {
        procedureDataSource.updateProcedures(*procedures)
    }

    override suspend fun deleteProcedures(vararg procedures: Procedure) {
        procedureDataSource.deleteProcedures(*procedures)
    }

    override suspend fun getProceduresWithNum(num: Int): List<Procedure> =
        procedureDataSource.getAllProceduresWithNum(num)

    override suspend fun getProcedouresWithCategoryNum(num: Int): List<Procedure> =
        procedureDataSource.getAllProceduresWithCategoryNum(num)

    override suspend fun getAllProceuduresWithCategoryNums(nums: List<Int>): List<Procedure> =
        procedureDataSource.getAllProceduresWithCategoryNums(nums)

    override fun getProcedureToFlowWithNum(num: Int): Flow<List<Procedure>> =
        procedureDataSource.getAllProceduresWithNumToFlow(num)
}