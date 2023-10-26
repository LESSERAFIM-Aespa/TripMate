package kr.sparta.tripmate.ui.budget.detail.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.tabs.TabLayoutMediator
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.databinding.ActivityBudgetDetailBinding
import kr.sparta.tripmate.ui.budget.BudgetContentActivity
import kr.sparta.tripmate.ui.budget.ProcedureContentActivity
import kr.sparta.tripmate.ui.viewmodel.budget.detail.main.BudgetDetailFactory
import kr.sparta.tripmate.ui.viewmodel.budget.detail.main.BudgetDetailViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.detail.procedure.BudgetProcedureFactory
import kr.sparta.tripmate.ui.viewmodel.budget.detail.procedure.BudgetProcedureViewModel

/**
 * 작성자: 서정한
 * 내용: 가계부 세부과정 Activity
 * */
class BudgetDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BUDGET = "extra_budget"

        fun newIntentForBudget(context: Context, model: Budget) =
            Intent(context, BudgetDetailActivity::class.java).apply {
                putExtra(EXTRA_BUDGET, model)
            }
    }

    private val binding by lazy {
        ActivityBudgetDetailBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        BudgetDetailViewPagerAdapter(this@BudgetDetailActivity)
    }

    private val viewModel: BudgetDetailViewModel by viewModels() {
        BudgetDetailFactory()
    }

    val budget by lazy {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_BUDGET, Budget::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_BUDGET)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        // TabLayout x ViewPager2
        budgetDetailViewpager.adapter = adapter
        TabLayoutMediator(budgetDetailTablayout, budgetDetailViewpager) { tab, position ->
            tab.setText(adapter.getTitle(position))
        }.attach()

        // 추가하기 FAB
        budgetDetailFloatingactionbutton.setOnClickListener {
            startActivity(
                ProcedureContentActivity.newIntentForAdd(
                    this@BudgetDetailActivity,
                    budget?.num ?: 0
                )
            )
        }

        // 수정 아이콘
        budgetDetailEditImageview.setOnClickListener {
            startActivity(
                BudgetContentActivity.newIntentForEdit(
                    context = this@BudgetDetailActivity,
                    budgetNum = budget?.num ?: 0
                )
            )
        }

        // 삭제 아이콘
        budgetDetailDeleteImageview.setOnClickListener {
            showDeleteBudgetDialog()
        }

        // 뒤로가기
        budgetDetailToolbar.setNavigationOnClickListener {
            finish()
        }

    }

    private fun showDeleteBudgetDialog() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.budget_detail_dialog_title))
        builder.setMessage(getString(R.string.budget_detail_dialog_message))

        // 버튼 클릭시에 무슨 작업을 할 것인가!
        val listener = object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        budget?.let {
                            viewModel.deleteBudget(it)
                            finish()
                        }
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }
        }

        builder.setPositiveButton(getString(R.string.budget_detail_dialog_positive_text), listener)
        builder.setNegativeButton(getString(R.string.budget_detail_dialog_negative_text), listener)

        builder.show()
    }
}