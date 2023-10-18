package kr.sparta.tripmate.ui.budget

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.DecimalFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.databinding.ActivityBudgetContentBinding
import java.util.Calendar

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

    private val budgetNum by lazy { intent.getIntExtra(EXTRA_BUDGET_NUM, 0) }
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        when (entryType) {
            BudgetContentType.ADD -> {
                budgetDetailTitleTextview.text = "가계부 추가"
            }

            BudgetContentType.EDIT -> {
                budgetDetailTitleTextview.text = "가계부 수정"
            }

            else -> {}
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
            val list = listOf<Category>(
                Category(budgetNum, "교통비", "#F4A261"),
                Category(budgetNum, "식비", "#E76F51"),
                Category(budgetNum, "기타", "#D9D9D9"),
            )
            categoryAdapter.saveList = list.toMutableList()
            categoryAdapter.submitList(list)

        }

        ItemTouchHelper(swipeHelper).attachToRecyclerView(budgetCategoryRecyclerview)
        budgetCategoryRecyclerview.setOnTouchListener { view, motionEvent ->
            swipeHelper.removePreviousClamp(budgetCategoryRecyclerview)
            false
        }

        budgetCategoryFloatingactionbutton.setOnClickListener {
            val currentList = categoryAdapter.currentList.toMutableList()
            val category = Category(budgetNum, "", "#E9C46A")
            currentList.add(category)
            categoryAdapter.saveList.add(category)
            categoryAdapter.submitList(currentList)
        }

        budgetDetailBackImageview.setOnClickListener {
            //뒤로가기
            finish()
        }

        budgetCancelButton.setOnClickListener {
            //취소하기
            finish()
        }

        budgetSaveButton.setOnClickListener {
            //저장버튼
            when (entryType) {
                BudgetContentType.ADD -> {

                }

                BudgetContentType.EDIT -> {

                }

                else -> {}
            }
            finish()
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
        val currentItem = categoryAdapter.currentList[pos]
        ColorPickerDialog
            .Builder(this)                        // Pass Activity Instance
            .setTitle("색상 선택")            // Default "Choose Color"
            .setPositiveButton("확인")
            .setNegativeButton("취소")
            .setColorShape(ColorShape.SQAURE)
            .setDefaultColor(currentItem.color)
            .setColorListener { _, colorHex ->
                button.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(colorHex))
            }
            .show()
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