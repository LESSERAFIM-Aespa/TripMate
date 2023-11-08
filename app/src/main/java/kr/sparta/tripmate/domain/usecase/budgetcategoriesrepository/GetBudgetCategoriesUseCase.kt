package kr.sparta.tripmate.domain.usecase.budgetcategoriesrepository

import kr.sparta.tripmate.data.model.budget.BudgetCategories
import kr.sparta.tripmate.domain.repository.budget.BudgetCategoriesRepository

class GetBudgetCategoriesUseCase(private val repository: BudgetCategoriesRepository) {
    suspend operator fun  invoke(num: Int) : List<BudgetCategories> = repository.getBudgetCategories(num)
}