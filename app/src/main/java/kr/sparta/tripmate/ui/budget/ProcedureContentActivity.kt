package kr.sparta.tripmate.ui.budget

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.DecimalFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.repository.BudgetRepositoryImpl
import kr.sparta.tripmate.databinding.ActivityBudgetContentBinding
import kr.sparta.tripmate.databinding.ActivityProcedureContentBinding
import kr.sparta.tripmate.ui.budget.BudgetContentActivity.Companion.EXTRA_BUDGET_ENTRY_TYPE
import kr.sparta.tripmate.ui.viewmodel.budget.ProcedureContentViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.ProcedureContentViewModelFactory
import kr.sparta.tripmate.util.method.toTimeFormat
import java.util.Calendar

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

    private lateinit var startDate: String
    private lateinit var endDate: String

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
                startDate = budget.startDate
                endDate = budget.endDate
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
        procedureTimeTextview.setOnClickListener {
            showDateAndTimePickerDialog(
                procedureTimeTextview.text.toString(),
                procedureTimeTextview
            )
        }
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

    private fun showDateAndTimePickerDialog(str: String, textView: TextView) {
        val isFirst = str == "시간과 날짜를 입력해 주세요"
        val listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            val date = "$year.${(month + 1).toTimeFormat()}.${day.toTimeFormat()}"
            if (date >= startDate && date <= endDate) {
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        textView.setTextColor(ContextCompat.getColor(this, R.color.black))
                        textView.text =
                            date + " ${hourOfDay.toTimeFormat()}:${minute.toTimeFormat()}"
                    }
                if (isFirst) {
                    TimePickerDialog(this, timeSetListener, 0, 0, true).apply {
                        setOnCancelListener {
                            showDateAndTimePickerDialogAgain(year, month, day, textView)
                        }
                    }.show()
                } else {
                    val (_, time) = str.split(" ")
                    val (hour, minute) = time.split(":")
                    TimePickerDialog(
                        this,
                        timeSetListener,
                        hour.toInt(),
                        minute.toInt(),
                        true
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "날짜는 시작일:$startDate 와 종료일:$endDate 사이로 해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (isFirst) {
            val arr = startDate.split(".")
            DatePickerDialog(
                this,
                listener,
                arr[0].toInt(),
                arr[1].toInt() - 1,
                arr[2].toInt()
            ).show()
        } else {
            val (date, _) = str.split(" ")
            val arr = date.split(".")
            DatePickerDialog(
                this,
                listener,
                arr[0].toInt(),
                arr[1].toInt() - 1,
                arr[2].toInt()
            ).show()
        }
    }

    private fun showDateAndTimePickerDialogAgain(
        year: Int,
        month: Int,
        day: Int,
        textView: TextView,
    ) {
        val listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            val date = "$year.${(month + 1).toTimeFormat()}.${day.toTimeFormat()}"
            if (date >= startDate && date <= endDate) {
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        textView.setTextColor(ContextCompat.getColor(this, R.color.black))
                        textView.text =
                            date + " ${hourOfDay.toTimeFormat()}:${minute.toTimeFormat()}"
                    }
                TimePickerDialog(
                    this,
                    timeSetListener,
                    0,
                    0,
                    true
                ).apply {
                    setOnCancelListener {
                        showDateAndTimePickerDialogAgain(
                            year,
                            month,
                            day,
                            textView
                        )
                    }
                }.show()
            } else {
                Toast.makeText(
                    this,
                    "날짜는 시작일:$startDate 와 종료일:$endDate 사이로 해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        DatePickerDialog(
            this,
            listener,
            year,
            month,
            day
        ).show()
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