package kr.sparta.tripmate.domain.usecase.categoryrepository

import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.domain.repository.budget.CategoryRepository
import javax.inject.Inject

class GetAllCategoriesWithBudgetNumUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(budgetNum: Int): List<Category> =
        repository.getAllCategoriesWithBudgetNum(budgetNum)
}