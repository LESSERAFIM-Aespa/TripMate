package kr.sparta.tripmate.ui.viewmodel.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.budget.BudgetCategories
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.domain.repository.BudgetRepository
import kr.sparta.tripmate.ui.budget.ProcedureContentType

class ProcedureContentViewModel(
    private val repository: BudgetRepository,
    private val entryType: ProcedureContentType,
    private val budgetNum: Int,
    private val procedureNum: Int = 0,
) : ViewModel() {

    private val _budgetCategories: MutableLiveData<List<BudgetCategories>> = MutableLiveData()
    val budgetCategories: LiveData<List<BudgetCategories>>
        get() = _budgetCategories

    private val _procedures: MutableLiveData<List<Procedure>> = MutableLiveData()
    val procedures: LiveData<List<Procedure>>
        get() = _procedures

    init {
        viewModelScope.launch {
            _budgetCategories.value = repository.getBudgetCategories(budgetNum)
            if (entryType == ProcedureContentType.EDIT) {
                _procedures.value = repository.getProceduresWithNum(procedureNum)
            }
        }
    }

    fun inserProcedure(procedure: Procedure) = viewModelScope.launch{
        repository.insertProcedures(procedure)
    }

    fun updateProcedure(procedure: Procedure) = viewModelScope.launch {
        if(procedures.value.orEmpty().first() != procedure){
            repository.updateProcedures(procedure)
        }
    }
}