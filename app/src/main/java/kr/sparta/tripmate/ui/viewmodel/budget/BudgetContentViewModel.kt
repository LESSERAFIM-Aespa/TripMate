package kr.sparta.tripmate.ui.viewmodel.budget

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.BudgetCategories
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository
import kr.sparta.tripmate.ui.budget.BudgetContentType

class BudgetContentViewModel(
    private val entryType: BudgetContentType,
    private val repository: BudgetRepository,
    private val budgetNum: Int = 0,
) : ViewModel() {
    companion object{
        private const val TAG = "BudgetContentViewModel"
    }

    private val _budgetCategories: MutableLiveData<List<BudgetCategories>> = MutableLiveData()
    val budgetCategories: LiveData<List<BudgetCategories>>
        get() = _budgetCategories

    init {
        when (entryType) {
            BudgetContentType.EDIT -> {
                viewModelScope.launch {
                    _budgetCategories.value = repository.getBudgetCategories(budgetNum)
                }
            }

            else -> {}
        }
    }

    fun inserBudgetAndCategories(budget: Budget, categories: List<Category>) =
        viewModelScope.launch {
            repository.insertBudgets(budget)
            val currentNum = repository.getLastBudget().first().num
            val arr = categories.map { it.copy(budgetNum = currentNum, num = 0) }.toTypedArray()
            repository.insertCategories(*arr)
        }

    fun updateBudgetAndCategories(budget: Budget, categories: List<Category>) =
        viewModelScope.launch {
            Log.d(TAG, "updateBudgetAndCategories: start")
            repository.updateBudgets(budget)
            val beforeCategories = budgetCategories.value.orEmpty().first().categories.orEmpty()

            val startTime = budget.startDate + " 00:00"
            val endTime = budget.endDate + " 23:59"
            Log.d(TAG, "updateBudgetAndCategories: $startTime $endTime")
            val beforeProcedures = repository.getAllProceuduresWithCategoryNums(beforeCategories.map { it.num })
                .map {
                    Log.d(TAG, "updateBudgetAndCategories: $it")
                when{
                    it.time >= endTime -> {
                        Log.d(TAG, "updateBudgetAndCategories: over")
                        it.copy(time = endTime)
                    }
                    it.time <= startTime -> {
                        Log.d(TAG, "updateBudgetAndCategories: under")
                        it.copy(time = startTime)
                    }
                    else -> {

                        Log.d(TAG, "updateBudgetAndCategories: state")
                        it
                    }
                }
            }.toTypedArray()
            
            repository.updateProcedures(*beforeProcedures)

            Log.d(TAG, "updateBudgetAndCategories: mid1")
            val checkedArr = BooleanArray(beforeCategories.size) { false }
            after@ for (category in categories) {
                for ((idx, beforeCategory) in beforeCategories.withIndex()) {
                    if (!checkedArr[idx] && category.num == beforeCategory.num) {
                        if (category == beforeCategory) {
                            checkedArr[idx] = true
                            continue@after
                        }
                        repository.updateCategories(category)
                        checkedArr[idx] = true
                        continue@after
                    }
                }
                repository.insertCategories(category.copy(num = 0))
            }
            Log.d(TAG, "updateBudgetAndCategories: mid2")
            val etcNum = beforeCategories[2].num // 기타 카테고리
            checkedArr.forEachIndexed { index, b ->
                if (!b){
                    val currentCategory = beforeCategories[index]
                    val procedures = repository.getPrcedouresWithCategoryNum(currentCategory.num)
                        .map { it.copy(categoryNum = etcNum) }.toTypedArray()
                    repository.updateProcedures(*procedures)
                    repository.deleteCategories(currentCategory)
                }
            }
            Log.d(TAG, "updateBudgetAndCategories: finish")
        }
}