package kr.sparta.tripmate.ui.budget

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityBudgetContentBinding
import kr.sparta.tripmate.databinding.ActivityProcedureContentBinding
import kr.sparta.tripmate.ui.budget.BudgetContentActivity.Companion.EXTRA_BUDGET_ENTRY_TYPE

class ProcedureContentActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_PROCEDURE_ENTRY_TYPE = "extra_procedure_entry_type"
        const val EXTRA_PROCEDURE_NUM = "extra_procedure_num"

        fun newIntentForAdd(context: Context) =
            Intent(context, ProcedureContentActivity::class.java).apply {
                putExtra(EXTRA_PROCEDURE_ENTRY_TYPE, ProcedureContentType.ADD.name)
            }

        fun newIntentForEdit(context: Context, procedureNum: Int) =
            Intent(context, ProcedureContentActivity::class.java).apply {
                putExtra(EXTRA_PROCEDURE_ENTRY_TYPE, ProcedureContentType.EDIT.name)
                putExtra(EXTRA_PROCEDURE_NUM, procedureNum)
            }
    }

    private val binding: ActivityProcedureContentBinding by lazy {
        ActivityProcedureContentBinding.inflate(layoutInflater)
    }

    private val entryType by lazy {
        ProcedureContentType.from(
            intent.getStringExtra(
                EXTRA_PROCEDURE_ENTRY_TYPE
            )
        )
    }

    private val procedureNum by lazy { intent.getIntExtra(EXTRA_PROCEDURE_NUM, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding){
        budgetDetailBackImageview.setOnClickListener {
            finish()
        }
        procedureCancelButton.setOnClickListener {
            finish()
        }
        procedureSaveButton.setOnClickListener {
            when(entryType){
                ProcedureContentType.ADD -> {

                }
                ProcedureContentType.EDIT -> {

                }
                else -> {}
            }
            finish()
        }
    }
}

enum class ProcedureContentType {
    ADD, EDIT;

    companion object {
        fun from(name: String?): ProcedureContentType? {
            return ProcedureContentType.values().find {
                it.name.uppercase() == name?.uppercase()
            }
        }
    }
}