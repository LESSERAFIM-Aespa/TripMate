package kr.sparta.tripmate.ui.budget

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import kr.sparta.tripmate.data.repository.budget.SaveRepositoryImpl
import kr.sparta.tripmate.databinding.ActivityProcedureDetailBinding
import kr.sparta.tripmate.ui.viewmodel.budget.ProcedureDetailViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.ProcedureDetailFactory
import kr.sparta.tripmate.util.method.toMoneyFormat
import kotlin.math.abs

class ProcedureDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_BUDGET_NUM = "extra_budget_num"
        const val EXTRA_PROCEDURE_NUM = "extra_procedure_num"

        fun newIntent(context: Context, budgetNum: Int, procedureNum: Int) =
            Intent(context, ProcedureDetailActivity::class.java).apply {
                putExtra(EXTRA_BUDGET_NUM, budgetNum)
                putExtra(EXTRA_PROCEDURE_NUM, procedureNum)
            }
    }

    private val binding by lazy {
        ActivityProcedureDetailBinding.inflate(layoutInflater)
    }
    private val budgetNum by lazy {
        intent.getIntExtra(EXTRA_BUDGET_NUM, 0)
    }
    private val procedureNum by lazy {
        intent.getIntExtra(EXTRA_PROCEDURE_NUM, 0)
    }

    private val procedureDetailViewModel: ProcedureDetailViewModel by viewModels {
        ProcedureDetailFactory(
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
        with(procedureDetailViewModel) {
            procedure.observe(this@ProcedureDetailActivity) { list ->
                if (list.isEmpty()) {
                    finish()
                }else{
                    val procedure = list.orEmpty().first()
                    val categories = categories.value.orEmpty()

                    binding.procedureToolbar.title = procedure.name
                    categories.firstOrNull {
                        it.num == procedure.categoryNum
                    }?.let { category ->
                        binding.procedureCategoryName.text = category.name
                        binding.procedureCategoryName.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor(category.color))
                    }
                    binding.procedureTimeTextview.text = procedure.time
                    binding.procedureDescriptionTextview.text = procedure.description
                    if (procedure.money > 0) {
                        binding.procedureMoneyStateTextview.text = "지출"
                    } else {
                        binding.procedureMoneyStateTextview.text = "수입"
                    }
                    binding.procedureMoneyTextview.text = abs(procedure.money).toMoneyFormat() + "원"
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        procedureToolbar.setNavigationOnClickListener {
            finish()
        }

        procedureDetailEditImageview.setOnClickListener {
            startActivity(
                ProcedureContentActivity.newIntentForEdit(
                    this@ProcedureDetailActivity, budgetNum, procedureNum
                )
            )
        }

        procedureDetailDeleteImageview.setOnClickListener {
            showDeleteDialog()
        }
    }

    // delete dialog가 나오게 적용
    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("삭제하기")
            setMessage(
                "삭제한 항목은 되돌릴 수 없습니다.\n" +
                        "삭제하시겠습니까?"
            )
            setNegativeButton("취소") { _, _ -> }
            setPositiveButton("확인") { _, _ ->
                procedureDetailViewModel.deleteProcedure()
                finish()
            }
        }
        builder.show()
    }
}