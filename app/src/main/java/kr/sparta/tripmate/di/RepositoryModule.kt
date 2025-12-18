package kr.sparta.tripmate.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.sparta.tripmate.data.repository.BlogRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.BudgetCategoriesRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.BudgetRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.BudgetTotalRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.CategoryRepositoryImpl
import kr.sparta.tripmate.data.repository.budget.ProcedureRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseBlogScrapRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseBoardScrapRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseStorageRepositoryImpl
import kr.sparta.tripmate.data.repository.sharedpreference.SharedPreferenceReopositoryImpl
import kr.sparta.tripmate.data.repository.user.FirebaseUserRepositoryImpl
import kr.sparta.tripmate.domain.repository.BlogRepository
import kr.sparta.tripmate.domain.repository.budget.BudgetCategoriesRepository
import kr.sparta.tripmate.domain.repository.budget.BudgetRepository
import kr.sparta.tripmate.domain.repository.budget.BudgetTotalRepository
import kr.sparta.tripmate.domain.repository.budget.CategoryRepository
import kr.sparta.tripmate.domain.repository.budget.ProcedureRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseBlogScrapRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardScrapRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseStorageRepository
import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // Blog Repository
    @Singleton
    @Binds
    abstract fun bindBlogRepository(impl: BlogRepositoryImpl): BlogRepository

    // Firebase Repositories
    @Singleton
    @Binds
    abstract fun bindFirebaseBlogScrapRepository(impl: FirebaseBlogScrapRepositoryImpl): FirebaseBlogScrapRepository

    @Singleton
    @Binds
    abstract fun bindFirebaseBoardRepository(impl: FirebaseBoardRepositoryImpl): FirebaseBoardRepository

    @Singleton
    @Binds
    abstract fun bindFirebaseBoardScrapRepository(impl: FirebaseBoardScrapRepositoryImpl): FirebaseBoardScrapRepository

    @Singleton
    @Binds
    abstract fun bindFirebaseUserRepository(impl: FirebaseUserRepositoryImpl): FirebaseUserRepository

    @Singleton
    @Binds
    abstract fun bindFirebaseStorageRepository(impl: FirebaseStorageRepositoryImpl): FirebaseStorageRepository

    // SharedPreference Repository
    @Singleton
    @Binds
    abstract fun bindSharedPreferenceRepository(impl: SharedPreferenceReopositoryImpl): SharedPreferenceReopository

    // Budget Repositories
    @Singleton
    @Binds
    abstract fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Singleton
    @Binds
    abstract fun bindProcedureRepository(impl: ProcedureRepositoryImpl): ProcedureRepository

    @Singleton
    @Binds
    abstract fun bindBudgetCategoriesRepository(impl: BudgetCategoriesRepositoryImpl): BudgetCategoriesRepository

    @Singleton
    @Binds
    abstract fun bindBudgetTotalRepository(impl: BudgetTotalRepositoryImpl): BudgetTotalRepository
}