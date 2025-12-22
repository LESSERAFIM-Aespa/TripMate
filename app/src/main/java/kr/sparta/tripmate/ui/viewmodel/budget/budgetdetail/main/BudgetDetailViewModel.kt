package kr.sparta.tripmate.ui.viewmodel.budget.budgetdetail.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.domain.usecase.budgetrepository.DeleteBudgetsUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetBudgetToFlowWhenBudgetChangedWithNumUseCase
import javax.inject.Inject

/**
 * 작성자: 서정한
 * 내용: 가계부 과정Fragment의 ViewModel
 * */
@HiltViewModel
class BudgetDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deleteBudgetsUseCase: DeleteBudgetsUseCase,
    getBudgetToFlowWhenBudgetChangedWithNumUseCase: GetBudgetToFlowWhenBudgetChangedWithNumUseCase,
) : ViewModel() {
    
    private val budgetNum: Int = savedStateHandle.get<Int>("extra_budget_num") ?: 0
    
    val budgetLiveData: LiveData<Budget> =
        getBudgetToFlowWhenBudgetChangedWithNumUseCase(budgetNum).asLiveData()

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