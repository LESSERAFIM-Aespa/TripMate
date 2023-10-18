package kr.sparta.tripmate.ui.budget

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kr.sparta.tripmate.databinding.ActivityBudgetDetailBinding
import kr.sparta.tripmate.ui.budget.detail.main.BudgetDetailViewPagerAdapter

/**
 * 작성자: 서정한
 * 내용: 가계부 세부과정 Activity
 * */
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

    private val adapter by lazy {
        BudgetDetailViewPagerAdapter(this@BudgetDetailActivity)
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
        // TabLayout x ViewPager2
        budgetDetailViewpager.adapter = adapter
        TabLayoutMediator(budgetDetailTablayout, budgetDetailViewpager) { tab, position ->
            tab.setText(adapter.getTitle(position))
        }.attach()

        // 추가하기 FAB
        budgetDetailFloatingactionbutton.setOnClickListener {
            startActivity(ProcedureContentActivity.newIntentForAdd(this@BudgetDetailActivity))
        }

        // 수정 아이콘
        budgetDetailEditImageview.setOnClickListener {
            startActivity(
                BudgetContentActivity.newIntentForEdit(
                    this@BudgetDetailActivity,
                    budgetNum
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

    }
}