package kr.sparta.tripmate.ui.budget

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityBudgetDetailBinding

class BudgetDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BUDGET_NUM = "extra_budget_num"


        //test 용,이동 확인후 삭제할것
        fun newIntent(context: Context) = Intent(context, BudgetDetailActivity::class.java)

        fun newIntent(context: Context, budgetNum: Int) =
            Intent(context, BudgetDetailActivity::class.java).apply {
                putExtra(EXTRA_BUDGET_NUM, budgetNum)
            }
    }

    private val binding by lazy {
        ActivityBudgetDetailBinding.inflate(layoutInflater)
    }

    private val budgetNum by lazy {
        intent.getIntExtra(EXTRA_BUDGET_NUM, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        budgetDetailFloatingactionbutton.setOnClickListener {
            startActivity(ProcedureContentActivity.newIntentForAdd(this@BudgetDetailActivity))
        }

        budgetDetailEditImageview.setOnClickListener {
            startActivity(BudgetContentActivity.newIntentForEdit(this@BudgetDetailActivity,budgetNum))
        }

        budgetDetailDeleteImageview.setOnClickListener {
            showDeleteBudgetDialog()
        }

        budgetDetailBackImageview.setOnClickListener {
            finish()
        }
    }

    private fun showDeleteBudgetDialog() {

    }
}