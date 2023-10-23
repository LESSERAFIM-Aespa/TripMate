package kr.sparta.tripmate.ui.viewmodel.budget.procedure

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.ui.budget.detail.procedure.ProcedureModel

/**
 * 작성자: 서정한
 * 내용: 가계부 과정Fragment의 ViewModel
 * */
class BudgetProcedureViewModel : ViewModel() {
    private val _procedureList : MutableLiveData<List<ProcedureModel>>  = MutableLiveData()
    private val procedureList get() = _procedureList
}