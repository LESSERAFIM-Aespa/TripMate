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
import kr.sparta.tripmate.domain.usecase.categoryrepository.GetAllCategoriesWithBudgetNumUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.DeleteProceduresUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.GetProcedureToFlowWithNumUseCase

class ProcedureDetailViewModel(
    private val deleteProceduresUseCase: DeleteProceduresUseCase,
    private val getProcedureToFlowWithNumUseCase: GetProcedureToFlowWithNumUseCase,
    private val getAllCategoriesWithBudgetNumUseCase: GetAllCategoriesWithBudgetNumUseCase,
    private val budgetNum: Int,
    private val procedureNum: Int,
) : ViewModel() {

    /*private val _budgetCategories: MutableLiveData<List<BudgetCategories>> = MutableLiveData()
    val budgetCategories: LiveData<List<BudgetCategories>>
        get() = _budgetCategories*/

    private val _categories: MutableLiveData<List<Category>> = MutableLiveData()
    val categories: LiveData<List<Category>>
        get() = _categories

    val procedure: LiveData<List<Procedure>> = flow {
        val value = getAllCategoriesWithBudgetNumUseCase(budgetNum)
        _categories.value = value
        getProcedureToFlowWithNumUseCase(procedureNum).collect {
            emit(it)
        }
    }.asLiveData()


    fun deleteProcedure() = viewModelScope.launch {
        deleteProceduresUseCase(procedure.value.orEmpty().first())
    }

}