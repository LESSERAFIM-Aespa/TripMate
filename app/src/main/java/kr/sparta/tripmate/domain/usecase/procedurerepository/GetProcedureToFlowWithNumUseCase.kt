package kr.sparta.tripmate.domain.usecase.procedurerepository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.budget.ProcedureRepository
import javax.inject.Inject

class GetProcedureToFlowWithNumUseCase @Inject constructor(private val repository: ProcedureRepository) {
    operator fun invoke(num: Int): Flow<List<Procedure>> =
        repository.getProcedureToFlowWithNum(num)
}