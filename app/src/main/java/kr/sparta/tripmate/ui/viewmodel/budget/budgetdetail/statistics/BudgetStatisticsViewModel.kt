package kr.sparta.tripmate.ui.viewmodel.budget.budgetdetail.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.data.model.budget.toModel
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetBudgetToFlowWhenBudgetChangedWithNumUseCase
import kr.sparta.tripmate.domain.usecase.budgettotalrepository.GetBudgetTotalToFlowWhenProccessChangedWithBudgetNumUseCase
import kr.sparta.tripmate.ui.budget.budgetdetail.procedure.ProcedureModel
import javax.inject.Inject

@HiltViewModel
class BudgetStatisticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getBudgetToFlowWhenBudgetChangedWithNumUseCase: GetBudgetToFlowWhenBudgetChangedWithNumUseCase,
    getBudgetTotalToFlowWhenProccessChangedWithBudgetNumUseCase: GetBudgetTotalToFlowWhenProccessChangedWithBudgetNumUseCase,
) : ViewModel() {
    
    private val budgetNum: Int = savedStateHandle.get<Int>("extra_budget_num") ?: 0

    private val _budgetLiveData: MutableLiveData<Budget> = MutableLiveData()
    val budgetLiveData get() = _budgetLiveData

    val budgetFlowToLiveData =
        getBudgetToFlowWhenBudgetChangedWithNumUseCase(budgetNum).flatMapConcat {
            flow {
                if (procedureList.value.orEmpty().isEmpty()) emit(it)
            }
        }.asLiveData()

    private val _procedureList: MutableLiveData<List<ProcedureModel>> = MutableLiveData()
    val procedureList get() = _procedureList

    val budgetTotal: LiveData<Triple<Budget, List<Category>, List<Procedure>>> =
        flow {
            getBudgetTotalToFlowWhenProccessChangedWithBudgetNumUseCase(budgetNum)
                .collect { triple ->
                    val budget = triple.first
                    val categories = triple.second
                    val procedures = triple.third

                    val categoriesMap: Map<Int, Category> = mutableMapOf<Int, Category>().apply {
                        categories.forEach {
                            put(it.num, it)
                        }
                    }

                    var currentMoney = budget.money.toLong()
                    fun reduceCurrentMoney(reduce: Int): Long {
                        currentMoney -= reduce
                        return currentMoney
                    }

                    val postList: List<ProcedureModel> = procedures.map {
                        it.toModel().copy(
                            beforeMoney = currentMoney,
                            totalAmount = reduceCurrentMoney(it.money),
                            categoryColor = categoriesMap[it.categoryNum]?.color ?: "#f8f8f8",
                            categoryName = categoriesMap[it.categoryNum]?.name ?: "",
                        )
                    }
                    _budgetLiveData.postValue(budget)
                    _procedureList.postValue(postList)

                    emit(triple)
                }
        }.asLiveData()
}