package kr.sparta.tripmate.ui.viewmodel.budget.procedure

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.toModel
import kr.sparta.tripmate.domain.repository.BudgetRepository
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
    fun getAllProcedures(model: Budget) {
        /**
         * 작성자: 서정한
         * 내용: 카테고리 item의 Color값을 Int로 불러옴
         * */
        fun getColorForCategory(category: Category?): Int =
            category?.let { model -> Color.parseColor(model.color) } ?: R.color.Gray

        /**
         * 작성자: 서정한
         * 내용: 카테고리 item의 이름을 불러옴
         * */
        fun getNameForCategory(category: Category?): String = category?.name ?: "기타"

        viewModelScope.launch {
            kotlin.runCatching {
                // 과정의 모든 데이터를 불러오기
                val procedures =
                    repository.getAllProceduresToFlowWhenProccessChangedWithBudgetNum(model.num)
                        .asLiveData().value

                // 과정데이터를 View에서 사용하는 Model로 변환
                val list = procedures?.map {
                    val category: Category? =
                        repository.getAllCategoriesForNum(it.categoryNum).firstOrNull()
                    val color = getColorForCategory(category)
                    val name = getNameForCategory(category)

                    // 직전 총액
                    var beforeMoney: Int = model.money
                    // 현재 잔액
                    val totalAmount: Int = beforeMoney - it.money
                    // 현재 총액 업데이트
                    beforeMoney = totalAmount
                    it.toModel(
                        ProcedureModel(
                            num = it.num,
                            title = it.name,
                            price = it.money,
                            beforeMoney = totalAmount + it.money,
                            totalAmount = totalAmount,
                            time = it.time,
                            categoryColor = color,
                            categoryName = name,
                        )
                    )
                }?.toList()
                _procedureList.value = list.orEmpty()
            }
                .onFailure {
                    Log.e("TripMate", "error: $it")
                }
        }
    }
}