package kr.sparta.tripmate.ui.viewmodel.budget.procedure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.api.naver.NaverNetWorkClient
import kr.sparta.tripmate.data.repository.ScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.ScrapRepository
import kr.sparta.tripmate.domain.usecase.GetSearchBlogUseCase

class BudgetProcedureFactory: ViewModelProvider.Factory {
    override fun<T : ViewModel> create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(BudgetProcedureViewModel::class.java)) {
            return BudgetProcedureViewModel() as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}