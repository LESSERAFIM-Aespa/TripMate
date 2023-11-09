package kr.sparta.tripmate.domain.usecase.procedurerepository

import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.budget.ProcedureRepository

class InsertProceduresUseCase(private val repository: ProcedureRepository) {
    suspend operator fun invoke(vararg procedures: Procedure) {
        repository.insertProcedures(*procedures)
    }
}