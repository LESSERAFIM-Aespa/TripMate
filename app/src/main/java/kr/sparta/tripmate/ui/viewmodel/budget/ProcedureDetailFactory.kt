package kr.sparta.tripmate.ui.viewmodel.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.budget.BudgetCategoriesLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.CategoryLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.repository.budget.CategoryRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.ProcedureRepositoryImpl
import kr.sparta.tripmate.domain.usecase.categoryrepository.GetAllCategoriesWithBudgetNumUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.DeleteProceduresUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.GetProcedureToFlowWithNumUseCase
import kr.sparta.tripmate.util.TripMateApp

class ProcedureDetailFactory(
    private val budgetNum: Int,
    private val procedureNum: Int,
) : ViewModelProvider.Factory {
    private val categoryRepository by lazy {
        CategoryRepositoryImpl(
            CategoryLocalDataSource(TripMateApp.getApp().applicationContext),
        )
    }

    private val procedureRepository by lazy {
        ProcedureRepositoryImpl(
            ProcedureLocalDataSource(TripMateApp.getApp().applicationContext),
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProcedureDetailViewModel::class.java)) {
            return ProcedureDetailViewModel(
                DeleteProceduresUseCase(procedureRepository),
                GetProcedureToFlowWithNumUseCase(procedureRepository),
                GetAllCategoriesWithBudgetNumUseCase(categoryRepository),
                budgetNum,
                procedureNum
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}