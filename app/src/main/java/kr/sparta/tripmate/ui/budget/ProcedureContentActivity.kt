package kr.sparta.tripmate.ui.budget

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.icu.text.DecimalFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.data.repository.budget.BudgetRepositoryImpl
import kr.sparta.tripmate.databinding.ActivityProcedureContentBinding
import kr.sparta.tripmate.ui.viewmodel.budget.ProcedureContentViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.ProcedureContentViewModelFactory
import kr.sparta.tripmate.util.method.toTimeFormat
import kotlin.math.abs

class ProcedureContentActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "ProcedureContentActivit"
        const val EXTRA_PROCEDURE_ENTRY_TYPE = "extra_procedure_entry_type"
        const val EXTRA_BUDGET_NUM = "extra_budget_num"
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

    private lateinit var arrayAdapter: ArrayAdapter<Category>

    private val decimalFormat = DecimalFormat("#,###")
    private var resultMoney: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initViewModels()
    }

    private fun initViewModels() {
        arrayAdapter = object : ArrayAdapter<Category>(this, R.layout.itme_spinner) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                with(view) {
                    findViewById<TextView>(R.id.spinner_textview).text = getItem(position)?.name
                }
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup,
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                with(view) {
                    findViewById<TextView>(R.id.spinner_textview).text = getItem(position)?.name
                }
                return view
            }

        }
        Log.d(TAG, "initViewModels: $budgetNum")
        with(procedureContentViewModel) {
            budgetCategories.observe(this@ProcedureContentActivity) { list ->
                val budgetCategories = list.orEmpty().first()
                val budget = budgetCategories.budget
                binding.procedureToolbar.title = budget.name
                startDate = budget.startDate
                endDate = budget.endDate
                val categories = budgetCategories.categories.orEmpty()
                arrayAdapter.addAll(Category(0, "카테고리를 정해 주세요", "", 0))
                arrayAdapter.addAll(categories)

                binding.procedureCategorySpinner.apply {
                    adapter = arrayAdapter
                }
            }
            if (entryType == ProcedureContentType.EDIT) {
                procedures.observe(this@ProcedureContentActivity) { list ->
                    val procedure = list.orEmpty().first()
                    binding.procedureNameEdittext.setText(procedure.name)
                    binding.procedureTimeTextview.text = procedure.time
                    if (procedure.money <= 0) {
                        binding.procedureMoneyTextview.isSelected = true
                        binding.procedureMoneyTextview.text = "소비"
                        binding.procedureMoneyTextview.backgroundTintList =
                            ColorStateList.valueOf(getColor(R.color.primary))
                    }
                    binding.procedureMoneyEdittext.setText("${abs(procedure.money)}")
                    binding.procedureMemoEdittext.setText(procedure.description)
                    loop@ for (i in 0 until arrayAdapter.count) {
                        val category = arrayAdapter.getItem(i)
                        if (category?.num == procedure.categoryNum) {
                            binding.procedureCategorySpinner.setSelection(i)
                            break@loop
                        }
                    }
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
        procedureToolbar.setNavigationOnClickListener {
            finish()
        }
        procedureCancelButton.setOnClickListener {
            finish()
        }
        procedureMoneyTextview.setOnClickListener {
            procedureMoneyTextview.apply {
                when (isSelected) {
                    true -> {
                        text = "지출"
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.secondary))
                    }

                    false -> {
                        text = "소득"
                        backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
                    }
                }
                isSelected = !isSelected
            }
        }

        binding.procedureMoneyEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != resultMoney) {
                    if (charSequence.toString().any { it == '.' }) {
                        resultMoney = decimalFormat.format(
                            charSequence.toString().replace(".", "").toDouble()
                        )
                        procedureMoneyEdittext.setText(resultMoney)
                        procedureMoneyEdittext.setSelection(resultMoney.length)
                    } else {
                        resultMoney = decimalFormat.format(
                            charSequence.toString().replace(",", "").toDouble()
                        )
                        procedureMoneyEdittext.setText(resultMoney)
                        procedureMoneyEdittext.setSelection(resultMoney.length)
                    }

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        procedureSaveButton.setOnClickListener {
            when {
                procedureNameEdittext.text.toString().isBlank() -> {
                    Toast.makeText(
                        this@ProcedureContentActivity,
                        "과정 이름이 공백입니다. 다시 확인해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                procedureNameEdittext.text.toString().length >= 10 -> {
                    Toast.makeText(
                        this@ProcedureContentActivity,
                        "과정 이름은 10자이내로 적어주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                procedureTimeTextview.text.toString() == "시간과 날짜를 입력해 주세요" -> {
                    Toast.makeText(
                        this@ProcedureContentActivity,
                        "시간을 확인해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                procedureCategorySpinner.selectedItemPosition == 0 -> {
                    Toast.makeText(
                        this@ProcedureContentActivity,
                        "카테고리를 선택해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                procedureMoneyEdittext.text.isNullOrBlank() -> {
                    Toast.makeText(
                        this@ProcedureContentActivity,
                        "금액이 비어있습니다. 확인해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                procedureMoneyEdittext.text.toString().replace(",", "").length > 9 -> {
                    Toast.makeText(
                        this@ProcedureContentActivity,
                        "최대범위를 넘었습니다. 10억 미만으로 입력해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val money = procedureMoneyEdittext.text.toString().replace(",", "").toInt()
                    val procedure = Procedure(
                        categoryNum = arrayAdapter.getItem(procedureCategorySpinner.selectedItemPosition)!!.num,
                        name = procedureNameEdittext.text.toString(),
                        description = procedureMemoEdittext.text.toString(),
                        money = if (binding.procedureMoneyTextview.isSelected) -money else money,
                        time = procedureTimeTextview.text.toString()
                    )
                    when (entryType) {
                        ProcedureContentType.ADD -> {
                            procedureContentViewModel.inserProcedure(procedure)
                        }

                        ProcedureContentType.EDIT -> {
                            procedureContentViewModel.updateProcedure(procedure.copy(num = procedureNum))
                        }

                        else -> {}
                    }
                    finish()
                }
            }
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