package kr.sparta.tripmate.domain.usecase.procedurerepository

import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.budget.ProcedureRepository

class DeleteProceduresUseCase(private val repository: ProcedureRepository) {
    suspend operator fun invoke(vararg procedures: Procedure) {
        repository.deleteProcedures(*procedures)
    }
}