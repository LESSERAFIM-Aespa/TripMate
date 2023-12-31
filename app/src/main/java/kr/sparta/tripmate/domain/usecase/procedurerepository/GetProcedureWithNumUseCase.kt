package kr.sparta.tripmate.domain.usecase.procedurerepository

import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.budget.ProcedureRepository

class GetProcedureWithNumUseCase(private val repository: ProcedureRepository) {
    suspend operator fun invoke(num: Int): List<Procedure> =
        repository.getProcedureWithNum(num)
}