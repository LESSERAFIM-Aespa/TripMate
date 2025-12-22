package kr.sparta.tripmate.ui.viewmodel.budget.procedurecontent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.budget.BudgetCategories
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.usecase.budgetcategoriesrepository.GetBudgetCategoriesUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.GetProcedureWithNumUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.InsertProceduresUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.UpdateProceduresUseCase
import kr.sparta.tripmate.ui.budget.procedurecontent.ProcedureContentType
import javax.inject.Inject

@HiltViewModel
class ProcedureContentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val insertProceduresUseCase: InsertProceduresUseCase,
    private val updateProceduresUseCase: UpdateProceduresUseCase,
    private val getBudgetCategoriesUseCase: GetBudgetCategoriesUseCase,
    private val getProcedureWithNumUsecase: GetProcedureWithNumUseCase,
) : ViewModel() {
    
    private val entryType: String? = savedStateHandle.get<String>("extra_procedure_entry_type")
    private val budgetNum: Int = savedStateHandle.get<Int>("extra_budget_num") ?: -1
    private val procedureNum: Int = savedStateHandle.get<Int>("extra_procedure_num") ?: -1

    private val _budgetCategories: MutableLiveData<List<BudgetCategories>> = MutableLiveData()
    val budgetCategories: LiveData<List<BudgetCategories>>
        get() = _budgetCategories

    private val _procedures: MutableLiveData<List<Procedure>> = MutableLiveData()
    val procedures: LiveData<List<Procedure>>
        get() = _procedures

    init {
        viewModelScope.launch {
            val budgetCategoriesValue = getBudgetCategoriesUseCase(budgetNum)
            _budgetCategories.value = budgetCategoriesValue
            if (entryType?.uppercase() == "EDIT") {
                val proceduresValue = getProcedureWithNumUsecase(procedureNum)
                _procedures.value = proceduresValue
            }
        }
    }

    fun inserProcedure(procedure: Procedure) = viewModelScope.launch {
        insertProceduresUseCase(procedure)
    }

    fun updateProcedure(procedure: Procedure) = viewModelScope.launch {
        if (procedures.value.orEmpty().first() != procedure) {
            updateProceduresUseCase(procedure)
        }
    }
}