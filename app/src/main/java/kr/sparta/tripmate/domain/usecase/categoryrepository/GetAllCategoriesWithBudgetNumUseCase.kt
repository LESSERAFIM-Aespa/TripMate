package kr.sparta.tripmate.domain.usecase.categoryrepository

import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.domain.repository.budget.CategoryRepository

class GetAllCategoriesWithBudgetNumUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(budgetNum: Int): List<Category> =
        repository.getAllCategoriesWithBudgetNum(budgetNum)
}