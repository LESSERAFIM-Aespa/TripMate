package kr.sparta.tripmate.ui.viewmodel.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.budget.BudgetCategoriesLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.BudgetLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.CategoryLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.CategoryProceduresLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.repository.budget.BudgetCategoriesRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.BudgetRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.CategoryRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.ProcedureRepositoryImpl
import kr.sparta.tripmate.domain.usecase.budgetcategoriesrepository.GetBudgetCategoriesUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.GetProceduresWithNumUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.InsertProceduresUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.UpdateProceduresUseCase
import kr.sparta.tripmate.ui.budget.ProcedureContentType
import kr.sparta.tripmate.util.TripMateApp


class ProcedureContentFactory(
    private val entryType: ProcedureContentType,
    private val budgetNum: Int,
    private val procedureNum: Int = 0,
) : ViewModelProvider.Factory {
    private val procedureRepository by lazy {
        ProcedureRepositoryImpl(
            ProcedureLocalDataSource(TripMateApp.getApp().applicationContext),
            BudgetCategoriesLocalDataSource(TripMateApp.getApp().applicationContext),
        )
    }

    private val budgetCategoriesRepository by lazy {
        BudgetCategoriesRepositoryImpl(
            BudgetCategoriesLocalDataSource(TripMateApp.getApp().applicationContext),
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProcedureContentViewModel::class.java)) {
            return ProcedureContentViewModel(
                InsertProceduresUseCase(procedureRepository),
                UpdateProceduresUseCase(procedureRepository),
                GetBudgetCategoriesUseCase(budgetCategoriesRepository),
                GetProceduresWithNumUseCase(procedureRepository),
                entryType,
                budgetNum,
                procedureNum
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}