package kr.sparta.tripmate.ui.viewmodel.budget.detail.procedure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.repository.budget.BudgetRepositoryImpl
import kr.sparta.tripmate.util.TripMateApp

class BudgetProcedureFactory(): ViewModelProvider.Factory {
    private val repository by lazy {
        BudgetRepositoryImpl(TripMateApp.getApp().applicationContext)
    }

    override fun<T : ViewModel> create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(BudgetProcedureViewModel::class.java)) {
            return BudgetProcedureViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}