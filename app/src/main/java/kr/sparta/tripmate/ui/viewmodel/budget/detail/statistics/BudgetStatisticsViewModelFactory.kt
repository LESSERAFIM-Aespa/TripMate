package kr.sparta.tripmate.ui.viewmodel.budget.detail.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.repository.BudgetRepositoryImpl
import kr.sparta.tripmate.domain.repository.BudgetRepository
import kr.sparta.tripmate.util.TripMateApp

class BudgetStatisticsViewModelFactory(val budgetNum:Int) : ViewModelProvider.Factory {

    private val repository by lazy {
        BudgetRepositoryImpl(TripMateApp.getApp().applicationContext)
    }
    override fun<T : ViewModel> create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(BudgetStatisticsViewModel::class.java)) {
            return BudgetStatisticsViewModel(budgetNum, repository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}