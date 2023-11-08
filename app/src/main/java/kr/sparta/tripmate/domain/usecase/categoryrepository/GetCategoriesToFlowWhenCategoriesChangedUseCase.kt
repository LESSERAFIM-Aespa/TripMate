package kr.sparta.tripmate.domain.usecase.categoryrepository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.domain.repository.budget.CategoryRepository

class GetCategoriesToFlowWhenCategoriesChangedUseCase(private val repository: CategoryRepository) {
    operator fun invoke(num: Int): Flow<List<Category>> =
        repository.getCategoriesToFlowWhenCategoriesChanged(num)
}