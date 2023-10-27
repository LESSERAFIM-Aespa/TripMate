package kr.sparta.tripmate.ui.viewmodel.budget.detail.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.BudgetRepository

class BudgetStatisticsViewModel(budgetNum:Int,repository: BudgetRepository) : ViewModel() {
    val budgetTotal : LiveData<Triple<Budget,List<Category>,List<Procedure>>> = repository.getBudgetTotalToFlowWhenProccessChangedWithBudgetNum(budgetNum).asLiveData()
}