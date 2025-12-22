package kr.sparta.tripmate.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.sparta.tripmate.data.datasource.local.budget.BudgetCategoriesLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.BudgetLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.CategoryLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.CategoryProceduresLocalDataSource
import kr.sparta.tripmate.data.datasource.local.budget.ProcedureLocalDataSource
import kr.sparta.tripmate.data.datasource.local.sharedpreference.SharedPreferencesLocalDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseStorageRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.scrap.FirebaseBlogScrapRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.user.FirebaseUserRemoteDataSource
import kr.sparta.tripmate.remote.source.BlogRemoteDataSource
import kr.sparta.tripmate.remote.source.BlogRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun bindBlogRemoteDataSource(impl: BlogRemoteDataSourceImpl): BlogRemoteDataSource

    companion object {
        // Local DataSources
        @Provides
        @Singleton
        fun provideSharedPreferencesLocalDataSource(
            @ApplicationContext context: Context
        ): SharedPreferencesLocalDataSource = SharedPreferencesLocalDataSource(context)

        @Provides
        @Singleton
        fun provideBudgetLocalDataSource(
            @ApplicationContext context: Context
        ): BudgetLocalDataSource = BudgetLocalDataSource(context)

        @Provides
        @Singleton
        fun provideCategoryLocalDataSource(
            @ApplicationContext context: Context
        ): CategoryLocalDataSource = CategoryLocalDataSource(context)

        @Provides
        @Singleton
        fun provideProcedureLocalDataSource(
            @ApplicationContext context: Context
        ): ProcedureLocalDataSource = ProcedureLocalDataSource(context)

        @Provides
        @Singleton
        fun provideBudgetCategoriesLocalDataSource(
            @ApplicationContext context: Context
        ): BudgetCategoriesLocalDataSource = BudgetCategoriesLocalDataSource(context)

        @Provides
        @Singleton
        fun provideCategoryProceduresLocalDataSource(
            @ApplicationContext context: Context
        ): CategoryProceduresLocalDataSource = CategoryProceduresLocalDataSource(context)

        // Remote DataSources
        @Provides
        @Singleton
        fun provideFirebaseBoardRemoteDataSource(): FirebaseBoardRemoteDataSource =
            FirebaseBoardRemoteDataSource()

        @Provides
        @Singleton
        fun provideFirebaseBlogScrapRemoteDataSource(): FirebaseBlogScrapRemoteDataSource =
            FirebaseBlogScrapRemoteDataSource()

        @Provides
        @Singleton
        fun provideFirebaseUserRemoteDataSource(): FirebaseUserRemoteDataSource =
            FirebaseUserRemoteDataSource()

        @Provides
        @Singleton
        fun provideFirebaseStorageRemoteDataSource(): FirebaseStorageRemoteDataSource =
            FirebaseStorageRemoteDataSource()
    }
}