package kr.sparta.tripmate.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kr.sparta.tripmate.data.dao.BudgetCategoriesDao
import kr.sparta.tripmate.data.dao.BudgetDao
import kr.sparta.tripmate.data.dao.CategoryDao
import kr.sparta.tripmate.data.dao.CategoryProceduresDao
import kr.sparta.tripmate.data.dao.ProcedureDao
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure

/**
 * 작성자 : 김성환
 * 가계부 데이터베이스입니다.
 * */
@Database(
    entities = [Budget::class, Category::class, Procedure::class],
    exportSchema = false,
    version = 1
)
abstract class BudgetDatabase : RoomDatabase() {
    abstract fun getBudgetDao(): BudgetDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getProcedureDao(): ProcedureDao
    abstract fun getBudgetCategoriesDao(): BudgetCategoriesDao
    abstract fun getCategoryProceduresDao(): CategoryProceduresDao

    companion object {
        private var INSTANCE: BudgetDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        fun getDatabsae(context: Context): BudgetDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context = context,
                    klass = BudgetDatabase::class.java,
                    name = "budget_database_1"
                ).addMigrations(MIGRATION_1_2)
                    .build()
            }
            return INSTANCE as BudgetDatabase
        }
    }

}