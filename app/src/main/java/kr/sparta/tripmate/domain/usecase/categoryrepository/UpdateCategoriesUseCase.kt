package kr.sparta.tripmate.domain.usecase.categoryrepository

import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.domain.repository.budget.CategoryRepository
import javax.inject.Inject

class UpdateCategoriesUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(vararg categories: Category) {
        repository.updateCategories(*categories)
    }
}