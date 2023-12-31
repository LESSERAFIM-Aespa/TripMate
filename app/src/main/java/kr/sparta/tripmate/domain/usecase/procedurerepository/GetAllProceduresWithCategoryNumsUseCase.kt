package kr.sparta.tripmate.domain.usecase.procedurerepository

import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.budget.ProcedureRepository

class GetAllProceduresWithCategoryNumsUseCase(private val repository: ProcedureRepository) {
    suspend operator fun invoke(nums: List<Int>): List<Procedure> =
        repository.getAllProceduresWithCategoryNums(nums)
}