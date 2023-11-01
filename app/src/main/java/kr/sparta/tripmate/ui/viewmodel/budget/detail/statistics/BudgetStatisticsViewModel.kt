package kr.sparta.tripmate.ui.viewmodel.budget.detail.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.play.integrity.internal.c
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.data.model.budget.toModel
import kr.sparta.tripmate.domain.repository.BudgetRepository
import kr.sparta.tripmate.ui.budget.detail.procedure.ProcedureModel

class BudgetStatisticsViewModel(budgetNum: Int, repository: BudgetRepository) : ViewModel() {
    private val _budgetLiveData: MutableLiveData<Budget> = MutableLiveData()
    val budgetLiveData get() = _budgetLiveData

    private val _procedureList: MutableLiveData<List<ProcedureModel>> = MutableLiveData()
    val procedureList get() = _procedureList

    val budgetTotal: LiveData<Triple<Budget, List<Category>, List<Procedure>>> =
        flow {
            repository.getBudgetTotalToFlowWhenProccessChangedWithBudgetNum(budgetNum)
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