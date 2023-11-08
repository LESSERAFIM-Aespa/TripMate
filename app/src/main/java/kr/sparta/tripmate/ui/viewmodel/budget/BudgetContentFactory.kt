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
import kr.sparta.tripmate.domain.repository.budget.SaveRepository
import kr.sparta.tripmate.domain.usecase.budgetcategoriesrepository.GetBudgetCategoriesUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.GetLastBudgetUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.InsertBudgetsUseCase
import kr.sparta.tripmate.domain.usecase.budgetrepository.UpdateBudgetsUseCase
import kr.sparta.tripmate.domain.usecase.categoryrepository.DeleteCategoriesUseCase
import kr.sparta.tripmate.domain.usecase.categoryrepository.InsertCategoriesUseCase
import kr.sparta.tripmate.domain.usecase.categoryrepository.UpdateCategoriesUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.GetAllProceuduresWithCategoryNumsUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.GetProcedouresWithCategoryNumUseCase
import kr.sparta.tripmate.domain.usecase.procedurerepository.UpdateProceduresUseCase
import kr.sparta.tripmate.ui.budget.BudgetContentType
import kr.sparta.tripmate.util.TripMateApp


class BudgetContentFactory(
    private val entryType: BudgetContentType,
    private val budgetNum: Int = 0,
) : ViewModelProvider.Factory {

    private val budgetRepository by lazy {
        BudgetRepositoryImpl(
            BudgetLocalDataSource(TripMateApp.getApp().applicationContext),
            ProcedureLocalDataSource(TripMateApp.getApp().applicationContext),
            CategoryProceduresLocalDataSource(TripMateApp.getApp().applicationContext),
        )
    }

    private val categoryRepository by lazy {
        CategoryRepositoryImpl(
            CategoryLocalDataSource(TripMateApp.getApp().applicationContext),
        )
    }

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
        if (modelClass.isAssignableFrom(BudgetContentViewModel::class.java)) {
            return BudgetContentViewModel(
                InsertBudgetsUseCase(budgetRepository),
                UpdateBudgetsUseCase(budgetRepository),
                InsertCategoriesUseCase(categoryRepository),
                UpdateCategoriesUseCase(categoryRepository),
                DeleteCategoriesUseCase(categoryRepository),
                UpdateProceduresUseCase(procedureRepository),
                GetBudgetCategoriesUseCase(budgetCategoriesRepository),
                GetLastBudgetUseCase(budgetRepository),
                GetProcedouresWithCategoryNumUseCase(procedureRepository),
                GetAllProceuduresWithCategoryNumsUseCase(procedureRepository),
                entryType,
                budgetNum
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}