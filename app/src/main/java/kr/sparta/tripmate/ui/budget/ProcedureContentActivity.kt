package kr.sparta.tripmate.ui.budget

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.repository.BudgetRepositoryImpl
import kr.sparta.tripmate.databinding.ActivityBudgetContentBinding
import kr.sparta.tripmate.databinding.ActivityProcedureContentBinding
import kr.sparta.tripmate.ui.budget.BudgetContentActivity.Companion.EXTRA_BUDGET_ENTRY_TYPE
import kr.sparta.tripmate.ui.viewmodel.budget.ProcedureContentViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.ProcedureContentViewModelFactory

class ProcedureContentActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_PROCEDURE_ENTRY_TYPE = "extra_procedure_entry_type"
        const val EXTRA_BUDGET_NUM = "extra_procedure_num"
        const val EXTRA_PROCEDURE_NUM = "extra_procedure_num"

        fun newIntentForAdd(context: Context, budgetNum: Int) =
            Intent(context, ProcedureContentActivity::class.java).apply {
                putExtra(EXTRA_PROCEDURE_ENTRY_TYPE, ProcedureContentType.ADD.name)
                putExtra(EXTRA_BUDGET_NUM, budgetNum)
            }

        fun newIntentForEdit(context: Context, budgetNum: Int, procedureNum: Int) =
            Intent(context, ProcedureContentActivity::class.java).apply {
                putExtra(EXTRA_PROCEDURE_ENTRY_TYPE, ProcedureContentType.EDIT.name)
                putExtra(EXTRA_BUDGET_NUM, budgetNum)
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

    private val budgetNum by lazy { intent.getIntExtra(EXTRA_BUDGET_NUM, -1) }
    private val procedureNum by lazy { intent.getIntExtra(EXTRA_PROCEDURE_NUM, -1) }

    private val procedureContentViewModel: ProcedureContentViewModel by viewModels {
        ProcedureContentViewModelFactory(
            BudgetRepositoryImpl(this),
            entryType!!,
            budgetNum,
            procedureNum
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initViewModels()
    }

    private fun initViewModels() {
        with(procedureContentViewModel) {
            budgetCategories.observe(this@ProcedureContentActivity) { list ->
                val budgetCategories = list.orEmpty().first()
                val budget = budgetCategories.budget
                binding.budgetDetailTitleTextview.text = budget.name
                val categories = budgetCategories.categories.orEmpty()
            }
            if (entryType == ProcedureContentType.EDIT) {
                procedures.observe(this@ProcedureContentActivity) { list ->
                    val procedure = list.orEmpty().first()
                    binding.procedureNameEdittext.setText(procedure.name)
                    binding.procedureTimeTextview.text = procedure.time
                    binding.procedureMemoEdittext.setText(procedure.description)
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        budgetDetailBackImageview.setOnClickListener {
            finish()
        }
        procedureCancelButton.setOnClickListener {
            finish()
        }
        procedureSaveButton.setOnClickListener {
            when (entryType) {
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