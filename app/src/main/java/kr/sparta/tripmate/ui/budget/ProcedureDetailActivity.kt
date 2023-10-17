package kr.sparta.tripmate.ui.budget

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityBudgetDetailBinding
import kr.sparta.tripmate.databinding.ActivityProcedureDetailBinding

class ProcedureDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_PROCEDURE_NUM = "extra_procedure_num"


        //test 용,이동 확인후 삭제할것
        fun newIntent(context: Context) =
            Intent(context, ProcedureDetailActivity::class.java)

        fun newIntent(context: Context, procedureNum: Int) =
            Intent(context, ProcedureDetailActivity::class.java).apply {
                putExtra(EXTRA_PROCEDURE_NUM, procedureNum)
            }
    }

    private val binding by lazy {
        ActivityProcedureDetailBinding.inflate(layoutInflater)
    }

    private val procedureNum = intent.getIntExtra(EXTRA_PROCEDURE_NUM, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {

    }
}