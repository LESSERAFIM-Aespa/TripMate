package kr.sparta.tripmate.ui.viewmodel.budget.detail.procedure

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.toModel
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository
import kr.sparta.tripmate.ui.budget.detail.procedure.ProcedureModel

/**
 * 작성자: 서정한
 * 내용: 가계부 과정Fragment의 ViewModel
 * */
class BudgetProcedureViewModel(private val repository: BudgetRepository) : ViewModel() {
    private val _procedureList: MutableLiveData<List<ProcedureModel>> = MutableLiveData()
    val procedureList get() = _procedureList

    /**
     * 작성자: 서정한
     * 내용: 가계부에 있는 모든 과정데이터를 불러옴.
     * */
    fun updateAllProcedures(model: Budget) {
        /**
         * 작성자: 서정한
         * 내용: 카테고리 item의 이름을 불러옴
         * */
        fun getNameForCategory(category: Category?): String = category?.name ?: "기타"


        kotlin.runCatching {
            viewModelScope.launch {
                // 원금
                val principal = model.money
                // 직전 총액
                var beforeAmount: Long = 0
                // 현재 남은 잔액
                var totalAmount: Long = principal.toLong()

                // 과정의 모든 데이터를 불러오기
                val procedures =
                    repository.getAllProceduresToFlowWhenProccessChangedWithBudgetNum(model.num)

                // 과정데이터를 View에서 사용하는 Model로 변환
                val list = procedures.mapIndexed { index, procedure ->
                    val category: Category? =
                        repository.getAllCategoriesForNum(procedure.categoryNum).firstOrNull()
                    val name = getNameForCategory(category)

                    // 가격
                    val price = procedure.money

                    beforeAmount = totalAmount
                    totalAmount -= price

                    procedure.toModel(
                        ProcedureModel(
                            num = procedure.num,
                            title = procedure.name,
                            price = procedure.money,
                            beforeMoney = beforeAmount,
                            totalAmount = totalAmount,
                            time = procedure.time,
                            categoryColor = category?.color ?: "#f8f8f8", // gray
                            categoryName = name,
                        )
                    )
                }.toList()
                _procedureList.value = list.orEmpty()
            }
        }
            .onFailure {
                Log.e("TripMate", "error: $it")
            }
    }

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