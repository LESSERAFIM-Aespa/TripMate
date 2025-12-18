package kr.sparta.tripmate.ui.budget.budgetcontent

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.DecimalFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.databinding.ActivityBudgetContentBinding
import kr.sparta.tripmate.ui.viewmodel.budget.budgetcontent.BudgetContentViewModel
import kr.sparta.tripmate.util.method.setMaxLength
import java.util.Calendar

@AndroidEntryPoint
class BudgetContentActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "BudgetContentActivity"
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

    private val budgetNum by lazy { intent.getIntExtra(EXTRA_BUDGET_NUM, 0) }

    private val contentViewModel: BudgetContentViewModel by viewModels()
    private val categoryAdapter by lazy {
        CategoryAdapter(object : CategoryAdapter.CategoryListEventListener {
            override fun onDeleteButtonClicked(pos: Int) {
                onDeleteCategory(pos)
            }

            override fun onColorButtonClicked(pos: Int, button: Button) {
                showColorPickerDialog(pos, button)
            }
        })
    }
    private val swipeHelper by lazy {
        SwipeHelper(categoryAdapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)
        }
    }

    private fun onDeleteCategory(pos: Int) {
        if (pos in 0..2) {
            swipeHelper.removePreviousClampAnyway(binding.budgetCategoryRecyclerview)
            Toast.makeText(this, "해당항목은 삭제할수없습니다", Toast.LENGTH_SHORT).show()
            return
        }
        val currentList = categoryAdapter.currentList.toMutableList()
        categoryAdapter.saveList.removeAt(pos)
        currentList.removeAt(pos)
        categoryAdapter.submitList(currentList)
    }


    private val decimalFormat = DecimalFormat("#,###")
    private var resultMoney: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setEditTextView()
        initViews()
        initViewModels()
    }


    private fun initViews() = with(binding) {
        when (entryType) {
            BudgetContentType.ADD -> {
                budgetContentToolbar.title = "가계부 추가"
            }

            BudgetContentType.EDIT -> {
                budgetContentToolbar.title = "가계부 수정"
            }

            else -> {}
        }

        budgetContentToolbar.setNavigationOnClickListener {
            finish()
        }

        budgetStartdateTextview.setOnClickListener {
            // 시작날짜
            val str = budgetStartdateTextview.text.toString()
            showDatePickerDialog(str, budgetStartdateTextview)
        }

        budgetEnddateTextview.setOnClickListener {
            // 종료날짜
            val str = budgetEnddateTextview.text.toString()
            showDatePickerDialog(str, budgetEnddateTextview)
        }

        budgetCategoryRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@BudgetContentActivity)
            adapter = categoryAdapter
            if (entryType == BudgetContentType.ADD) {
                val list = listOf<Category>(
                    Category(budgetNum, "교통비", "#F4A261", -1),
                    Category(budgetNum, "식비", "#E76F51", -2),
                    Category(budgetNum, "기타", "#D9D9D9", -3),
                )
                categoryAdapter.saveList = list.toMutableList()
                categoryAdapter.submitList(list)
            }
        }

        ItemTouchHelper(swipeHelper).attachToRecyclerView(budgetCategoryRecyclerview)
        budgetCategoryRecyclerview.setOnTouchListener { view, motionEvent ->
            swipeHelper.removePreviousClamp(budgetCategoryRecyclerview)
            false
        }
        var saveCategoryNum = -4
        budgetCategoryFloatingactionbutton.setOnClickListener {
            val currentList = categoryAdapter.currentList.toMutableList()
            val category = Category(budgetNum, "", "#D9D9D9", saveCategoryNum--)
            currentList.add(category)
            categoryAdapter.saveList.add(category)
            categoryAdapter.submitList(currentList)
        }



        budgetCancelButton.setOnClickListener {
            //취소하기
            finish()
        }
        budgetMoneyEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != resultMoney) {
                    if (charSequence.toString().any { it == '.' }) {
                        resultMoney = decimalFormat.format(
                            charSequence.toString().replace(".", "").toDouble()
                        )
                        budgetMoneyEdittext.setText(resultMoney)
                        budgetMoneyEdittext.setSelection(resultMoney.length)
                    } else {
                        resultMoney = decimalFormat.format(
                            charSequence.toString().replace(",", "").toDouble()
                        )
                        budgetMoneyEdittext.setText(resultMoney)
                        budgetMoneyEdittext.setSelection(resultMoney.length)
                    }

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        budgetSaveButton.setOnClickListener {
            when {
                budgetNameEdittext.text.toString().isBlank() -> {
                    Toast.makeText(
                        this@BudgetContentActivity,
                        "가계부 이름이 공백입니다. 다시 확인해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                budgetNameEdittext.text.toString().length >= 14 -> {
                    Toast.makeText(
                        this@BudgetContentActivity,
                        "가계부 이름은 13자이내로 적어주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                budgetStartdateTextview.text.toString() == "날짜를 입력해 주세요" || budgetEnddateTextview.text.toString() == "날짜를 입력해 주세요" -> {
                    Toast.makeText(
                        this@BudgetContentActivity,
                        "날짜를 확인해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                budgetStartdateTextview.text.toString() > budgetEnddateTextview.text.toString() -> {
                    Toast.makeText(
                        this@BudgetContentActivity,
                        "시작일이 종료일보다 큽니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                budgetMoneyEdittext.text.toString().isBlank() -> {
                    Toast.makeText(
                        this@BudgetContentActivity,
                        "원금이 비어있습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !budgetMoneyEdittext.text.toString().replace(",", "").isDigitsOnly() -> {
                    Toast.makeText(
                        this@BudgetContentActivity,
                        "소수와 음수는 원금으로 사용할수없습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                budgetMoneyEdittext.text.toString().replace(",", "").length > 9 -> {
                    Toast.makeText(
                        this@BudgetContentActivity,
                        "최대범위를 넘었습니다. 10억 미만으로 입력해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                categoryAdapter.saveList.any { it.name.isBlank() || it.name.length > 10 } -> {
                    Toast.makeText(
                        this@BudgetContentActivity,
                        "카테고리이름을 10자이내로 적어주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val budget = Budget(
                        name = budgetNameEdittext.text.toString(),
                        startDate = budgetStartdateTextview.text.toString(),
                        endDate = budgetEnddateTextview.text.toString(),
                        money = budgetMoneyEdittext.text.toString().replace(",", "").toInt()
                    )
                    val categories = categoryAdapter.saveList
                    when (entryType) {
                        BudgetContentType.ADD -> {
                            contentViewModel.inserBudgetAndCategories(budget, categories)
                            finish()
                        }

                        BudgetContentType.EDIT -> {
                            lifecycleScope.launch {
                                contentViewModel.updateBudgetAndCategories(
                                    budget.copy(num = budgetNum),
                                    categories
                                )
                                finish()
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun initViewModels() {
        when (entryType) {
            BudgetContentType.EDIT -> {
                with(contentViewModel) {
                    budgetCategories.observe(this@BudgetContentActivity) {
                        val budgetCategory = it.orEmpty()[0]
                        val budget = budgetCategory.budget
                        binding.budgetNameEdittext.setText(budget.name)
                        binding.budgetStartdateTextview.text = budget.startDate
                        binding.budgetEnddateTextview.text = budget.endDate
                        binding.budgetMoneyEdittext.setText(budget.money.toString())
                        val categories = budgetCategory.categories.orEmpty()
                        categoryAdapter.saveList = categories.toMutableList()
                        categoryAdapter.submitList(categories)
                    }
                }
            }

            else -> {}
        }
    }

    private fun showDatePickerDialog(str: String, textView: TextView) {
        val df1 = DecimalFormat("00")
        val listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            textView.text = "$year.${df1.format(month + 1)}.${df1.format(day)}"
            textView.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        if (str.isBlank() || str == "날짜를 입력해 주세요") {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                listener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        } else {
            val arr = str.split(".")
            DatePickerDialog(
                this,
                listener,
                arr[0].toInt(),
                arr[1].toInt() - 1,
                arr[2].toInt()
            ).show()
        }
    }

    private fun showColorPickerDialog(pos: Int, button: Button) {

        ColorPickerDialog
            .Builder(this)                        // Pass Activity Instance
            .setTitle("색상 선택")            // Default "Choose Color"
            .setPositiveButton("확인")
            .setNegativeButton("취소")
            .setColorShape(ColorShape.SQAURE)
            .setDefaultColor(categoryAdapter.saveList[pos].color)
            .setColorListener { _, colorHex ->

                val currentItem = categoryAdapter.saveList[pos]
                button.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(colorHex))
                categoryAdapter.saveList[pos] = currentItem.copy(color = colorHex)
            }
            .show()
    }
    private fun setEditTextView() = with(binding) {
        budgetMoneyEdittext.setMaxLength(13)
        budgetNameEdittext.setMaxLength(13)
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