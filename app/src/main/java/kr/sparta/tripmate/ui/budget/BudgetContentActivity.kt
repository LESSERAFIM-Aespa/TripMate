package kr.sparta.tripmate.ui.budget

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityBudgetContentBinding
import kr.sparta.tripmate.databinding.ActivityBudgetDetailBinding

class BudgetContentActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_BUDGET_ENTRY_TYPE = "extra_budget_entry_type"
        const val EXTRA_BUDGET_NUM = "extra_budget_num"

        fun newIntentForAdd(context: Context) =
            Intent(context, BudgetContentActivity::class.java).apply {
                putExtra(EXTRA_BUDGET_ENTRY_TYPE, BudgetContentType.ADD.name)
            }

        fun newIntentForEdit(context: Context, budgetNum: Int) =
            Intent(context, BudgetContentActivity::class.java).apply {
                putExtra(EXTRA_BUDGET_ENTRY_TYPE, BudgetContentType.EDIT.name)
                putExtra(EXTRA_BUDGET_NUM, budgetNum)
            }
    }

    private val binding: ActivityBudgetContentBinding by lazy {
        ActivityBudgetContentBinding.inflate(layoutInflater)
    }

    private val entryType by lazy {
        BudgetContentType.from(
            intent.getStringExtra(
                EXTRA_BUDGET_ENTRY_TYPE
            )
        )
    }

    private val budgetNum by lazy { intent.getIntExtra(EXTRA_BUDGET_NUM, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root)

        initViews()
    }

    private fun initViews() = with(binding){
        when(entryType){
            BudgetContentType.ADD -> budgetDetailTitleTextview.text = "가계부 추가"
            BudgetContentType.EDIT -> budgetDetailTitleTextview.text = "가계부 수정"
            else -> {}
        }

        budgetDetailBackImageview.setOnClickListener {
            finish()
        }

        budgetCancelButton.setOnClickListener {
            finish()
        }

        budgetSaveButton.setOnClickListener {
            when(entryType){
                BudgetContentType.ADD -> {

                }
                BudgetContentType.EDIT -> {

                }
                else -> {}
            }
            finish()
        }
    }
}

enum class BudgetContentType {
    ADD, EDIT;

    companion object {
        fun from(name: String?): BudgetContentType? {
            return BudgetContentType.values().find {
                it.name.uppercase() == name?.uppercase()
            }
        }
    }
}