package kr.sparta.tripmate.di

import android.content.Context
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.sparta.tripmate.data.dao.BudgetCategoriesDao
import kr.sparta.tripmate.data.dao.BudgetDao
import kr.sparta.tripmate.data.dao.CategoryDao
import kr.sparta.tripmate.data.dao.CategoryProceduresDao
import kr.sparta.tripmate.data.dao.ProcedureDao
import kr.sparta.tripmate.data.datasource.local.BudgetDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RoomDatabase =
        BudgetDatabase.getDatabsae(context)

    @Provides
    fun provideBudgetDao(database: BudgetDatabase): BudgetDao = database.getBudgetDao()

    @Provides
    fun provideCategoryDao(database: BudgetDatabase): CategoryDao = database.getCategoryDao()

    @Provides
    fun provideProcedureDao(database: BudgetDatabase): ProcedureDao = database.getProcedureDao()

    @Provides
    fun provideBudgetCategoriesDao(database: BudgetDatabase): BudgetCategoriesDao =
        database.getBudgetCategoriesDao()

    @Provides
    fun provideCategoryProceduresDao(database: BudgetDatabase): CategoryProceduresDao =
        database.getCategoryProceduresDao()
}