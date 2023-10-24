package kr.sparta.tripmate.ui.viewmodel.budget.detail.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.repository.BudgetRepositoryImpl
import kr.sparta.tripmate.util.TripMateApp

class BudgetDetailFactory(): ViewModelProvider.Factory {
    private val repository by lazy {
        BudgetRepositoryImpl(TripMateApp.getApp().applicationContext)
    }

    override fun<T : ViewModel> create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(BudgetDetailViewModel::class.java)) {
            return BudgetDetailViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}