package kr.sparta.tripmate.ui.viewmodel.budget.proceduredetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.usecase.categoryrepository.GetAllCategoriesWithBudgetNumUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.DeleteProceduresUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.GetProcedureToFlowWithNumUseCase
import javax.inject.Inject

@HiltViewModel
class ProcedureDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deleteProceduresUseCase: DeleteProceduresUseCase,
    private val getProcedureToFlowWithNumUseCase: GetProcedureToFlowWithNumUseCase,
    private val getAllCategoriesWithBudgetNumUseCase: GetAllCategoriesWithBudgetNumUseCase,
) : ViewModel() {
    
    private val budgetNum: Int = savedStateHandle.get<Int>("extra_budget_num") ?: 0
    private val procedureNum: Int = savedStateHandle.get<Int>("extra_procedure_num") ?: 0

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