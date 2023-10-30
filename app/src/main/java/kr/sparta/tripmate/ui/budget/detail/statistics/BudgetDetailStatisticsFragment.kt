package kr.sparta.tripmate.ui.budget.detail.statistics

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.databinding.FragmentBudgetDetailStatisticsBinding
import kr.sparta.tripmate.ui.viewmodel.budget.detail.statistics.BudgetStatisticsViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.detail.statistics.BudgetStatisticsViewModelFactory
import kr.sparta.tripmate.util.method.toMoneyFormat
import java.text.NumberFormat

private const val ARG_BUDGET_NUM = "budgetNum"

class BudgetDetailStatisticsFragment : Fragment() {

    companion object {
        private const val TAG = "BudgetDetailStatisticsFragment"
        fun newInstance(budgetNum: Int) = BudgetDetailStatisticsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_BUDGET_NUM, budgetNum)
            }
        }
    }

    private val budgetNum: Int by lazy {
        arguments?.getInt(ARG_BUDGET_NUM)!!
    }

    private var _binding: FragmentBudgetDetailStatisticsBinding? = null
    private val binding: FragmentBudgetDetailStatisticsBinding
        get() = _binding!!

    private val viewModel: BudgetStatisticsViewModel by activityViewModels {
        BudgetStatisticsViewModelFactory(budgetNum)
    }

    private val expenditureListAdapter : BudgetDetailStatisticsListAdapter = BudgetDetailStatisticsListAdapter()
    private val incomeListAdapter : BudgetDetailStatisticsListAdapter = BudgetDetailStatisticsListAdapter()

    fun PieDataSet.toCustomFormat() = this.apply{
        valueTextSize = 16f

        // Value lines
        valueLinePart1Length = 0.6f
        valueLinePart2Length = 0.3f
        valueLineWidth = 2f
        valueLinePart1OffsetPercentage = 115f
        isUsingSliceColorAsValueLineColor = true
        // Value text appearance
        yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        valueTextSize = 16f
        valueTypeface = Typeface.DEFAULT_BOLD
        valueFormatter = object : ValueFormatter() {
            private val formatter = NumberFormat.getPercentInstance()

            override fun getFormattedValue(value: Float) =
                formatter.format(value / 100f)
        }
        selectionShift = 3f
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBudgetDetailStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModels()
    }

    private fun initViews() = with(binding) {
        binding.budgetDetailExpenditurePiechart.apply {
            renderer = CustomPieChartRenderer(this, 10f)
            setExtraOffsets(40f, 12f, 40f, 12f)

            isDrawHoleEnabled = true
            holeRadius = 50f
            setDrawCenterText(true)
            setCenterTextSize(20f)
            setCenterTextTypeface(Typeface.DEFAULT_BOLD)
            setCenterTextColor(Color.parseColor("#222222"))
            centerText = "지출"

            legend.isEnabled = false
            description.isEnabled = false
            isRotationEnabled = false

            setUsePercentValues(true)
            setEntryLabelColor(Color.BLACK)
            setTouchEnabled(false)
        }

        binding.budgetDetailIncomePiechart.apply {
            renderer = CustomPieChartRenderer(this, 10f)
            setExtraOffsets(40f, 12f, 40f, 12f)

            isDrawHoleEnabled = true
            holeRadius = 50f
            setDrawCenterText(true)
            setCenterTextSize(20f)
            setCenterTextTypeface(Typeface.DEFAULT_BOLD)
            setCenterTextColor(Color.parseColor("#222222"))
            centerText = "소득"

            legend.isEnabled = false
            description.isEnabled = false
            isRotationEnabled = false

            setUsePercentValues(true)
            setEntryLabelColor(Color.BLACK)
            setTouchEnabled(false)
        }

        binding.budgetDetailExpenditureRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expenditureListAdapter
        }

        binding.budgetDetailIncomeRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = incomeListAdapter
        }
    }

    private fun initViewModels() {
        with(viewModel) {
            budgetTotal.observe(viewLifecycleOwner) { budgetTotal ->
                val budget: Budget = budgetTotal.first
                val categories: List<Category> = budgetTotal.second
                val categoryMap: Map<Int, Category> =
                    mutableMapOf<Int, Category>().apply { categories.forEach { put(it.num, it) } }

                val procedures: List<Procedure> = budgetTotal.third
                val procedureGroupByCategoryNum: Map<Int, List<Procedure>> =
                    procedures.groupBy { it.categoryNum }

                // 지출
                val totalExpenditureData : MutableMap<Int,Int> = mutableMapOf()
                // 수입
                val totalIncomeData : MutableMap<Int,Int> = mutableMapOf()

                procedureGroupByCategoryNum.forEach { i, procedures ->
                    var expenditure = 0
                    var income = 0

                    procedures.forEach { procedure ->
                        if (procedure.money < 0){//수입
                            income += -procedure.money
                        }else if (procedure.money > 0){//지출
                            expenditure += procedure.money
                        }
                    }

                    if (expenditure != 0) totalExpenditureData[i] = expenditure
                    if (income != 0) totalIncomeData[i] = income
                }

                val totlaExpenditureSum = totalExpenditureData.map { it.value }.sumOf { it }
                val totlaIncomeSum = totalIncomeData.map { it.value }.sumOf { it }

                if (totalExpenditureData.isNotEmpty()){
                    binding.budgetDetailExpenditurePiechart.visibility = View.VISIBLE
                    binding.budgetDetailExpenditureRecyclerview.visibility = View.VISIBLE
                    binding.expenditureTitleTextview.visibility = View.VISIBLE
                    binding.expenditureTextview.visibility = View.VISIBLE

                    val entries = ArrayList<PieEntry>()
                    val colorsItems = ArrayList<Int>()

                    val adapterPostItems = mutableListOf<Pair<Category,String>>()
                    totalExpenditureData.forEach { key, sum ->
                        entries.add(PieEntry(sum.toFloat(),categoryMap[key]?.name))
                        Log.d(TAG, "initViewModels: currentEntry ${entries.last().value} ${entries.last().label}")
                        colorsItems.add(Color.parseColor(categoryMap[key]?.color))

                        adapterPostItems.add(Pair(categoryMap[key]!!,"${(sum/totlaExpenditureSum.toFloat()*100).toInt()}%, ${sum.toMoneyFormat()}원"))
                    }

                    expenditureListAdapter.submitList(adapterPostItems)

                    val pieDataSet = PieDataSet(entries, "").apply {
                        colors = colorsItems
                        setValueTextColors(colorsItems)
                    }.toCustomFormat()

                    val pieData = PieData(pieDataSet)
                    binding.budgetDetailExpenditurePiechart.data = pieData
                    binding.budgetDetailExpenditurePiechart.data.setValueFormatter(PercentFormatter())
                    binding.budgetDetailExpenditurePiechart.animate()
                    binding.budgetDetailExpenditurePiechart.invalidate()
                    binding.expenditureTextview.text = totlaExpenditureSum.toMoneyFormat() + "원"
                } else {
                    binding.budgetDetailExpenditurePiechart.visibility = View.GONE
                    binding.budgetDetailExpenditureRecyclerview.visibility = View.GONE
                    binding.expenditureTitleTextview.visibility = View.GONE
                    binding.expenditureTextview.visibility = View.GONE
                }

                if (totalIncomeData.isNotEmpty()){
                    binding.budgetDetailIncomePiechart.visibility = View.VISIBLE
                    binding.budgetDetailIncomeRecyclerview.visibility = View.VISIBLE
                    binding.incomeTitleTextview.visibility = View.VISIBLE
                    binding.incomeTextview.visibility = View.VISIBLE

                    val entries = ArrayList<PieEntry>()
                    val colorsItems = ArrayList<Int>()

                    val adapterPostItems = mutableListOf<Pair<Category,String>>()
                    totalIncomeData.forEach { key, sum ->
                        entries.add(PieEntry(sum.toFloat(),categoryMap[key]?.name))
                        Log.d(TAG, "initViewModels: currentEntry ${entries.last().value} ${entries.last().label}")
                        colorsItems.add(Color.parseColor(categoryMap[key]?.color))

                        adapterPostItems.add(Pair(categoryMap[key]!!,"${(sum/totlaIncomeSum.toFloat()*100).toInt()}%, ${sum.toMoneyFormat()}원"))
                    }

                    incomeListAdapter.submitList(adapterPostItems)

                    val pieDataSet = PieDataSet(entries, "").apply {
                        colors = colorsItems
                        setValueTextColors(colorsItems)
                    }.toCustomFormat()

                    val pieData = PieData(pieDataSet)
                    binding.budgetDetailIncomePiechart.data = pieData
                    binding.budgetDetailIncomePiechart.data.setValueFormatter(PercentFormatter())

                    binding.budgetDetailIncomePiechart.animate()
                    binding.budgetDetailIncomePiechart.invalidate()

                    binding.incomeTextview.text = totlaIncomeSum.toMoneyFormat() + "원"
                } else {
                    binding.budgetDetailIncomePiechart.visibility = View.GONE
                    binding.budgetDetailIncomeRecyclerview.visibility = View.GONE
                    binding.incomeTitleTextview.visibility = View.GONE
                    binding.incomeTextview.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}