package kr.sparta.tripmate.domain.usecase.categoryrepository

import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.domain.repository.budget.CategoryRepository

class GetAllCategoriesForNumUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(num: Int): List<Category> =
        repository.getAllCategoriesForNum(num)
}