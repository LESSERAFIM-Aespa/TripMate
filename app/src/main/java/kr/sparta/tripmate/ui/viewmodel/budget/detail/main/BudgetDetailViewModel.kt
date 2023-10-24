package kr.sparta.tripmate.ui.viewmodel.budget.detail.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.toModel
import kr.sparta.tripmate.domain.repository.BudgetRepository
import kr.sparta.tripmate.ui.budget.detail.procedure.ProcedureModel

/**
 * 작성자: 서정한
 * 내용: 가계부 과정Fragment의 ViewModel
 * */
class BudgetDetailViewModel(private val repository: BudgetRepository) : ViewModel() {
    /**
     * 작성자: 서정한
     * 내용: 현재 가계부를 삭제합니다.
     * */
    fun deleteBudget(budget: Budget) {
        kotlin.runCatching {
            viewModelScope.launch {
                repository.deleteBugets(budget)
            }
        }
            .onFailure {
                Log.e("TripMate", "error: $it")
            }
    }
}