package kr.sparta.tripmate.ui.viewmodel.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository

class ProcedureDetailViewModel(
    private val repository: BudgetRepository,
    private val budgetNum: Int,
    private val procedureNum: Int,
) : ViewModel() {

    /*private val _budgetCategories: MutableLiveData<List<BudgetCategories>> = MutableLiveData()
    val budgetCategories: LiveData<List<BudgetCategories>>
        get() = _budgetCategories*/

    private val _categories: MutableLiveData<List<Category>> = MutableLiveData()
    val categories: LiveData<List<Category>>
        get() = _categories

    val procedure: LiveData<List<Procedure>> = flow{
        _categories.value = repository.getAllCategoriesWithBudgetNum(budgetNum)
        repository.getProcedureToFlowWithNum(procedureNum).collect{
            emit(it)
        }
    }.asLiveData()


    fun deleteProcedure() = viewModelScope.launch {
        repository.deleteProcedures(procedure.value.orEmpty().first())
    }

}