package kr.sparta.tripmate.ui.viewmodel.budget.detail.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.usecase.budgetrepository.DeleteBudgetsUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetBugetToFlowWhenBudgetChangedWithNumUseCase

/**
 * 작성자: 서정한
 * 내용: 가계부 과정Fragment의 ViewModel
 * */
class BudgetDetailViewModel(
    private val deleteBudgetsUseCase: DeleteBudgetsUseCase,
    getBugetToFlowWhenBudgetChangedWithNumUseCase: GetBugetToFlowWhenBudgetChangedWithNumUseCase,
    budgetNum: Int,
) : ViewModel() {
    val budgetLiveData: LiveData<Budget> =
        getBugetToFlowWhenBudgetChangedWithNumUseCase(budgetNum).asLiveData()

    /**
     * 작성자: 서정한
     * 내용: 현재 가계부를 삭제합니다.
     * */
    fun deleteBudget(budget: Budget) {
        kotlin.runCatching {
            viewModelScope.launch {
                deleteBudgetsUseCase(budget)
            }
        }.onFailure {
            Log.e("TripMate", "error: $it")
        }
    }
}