package kr.sparta.tripmate.ui.budget.detail.statistics

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.play.integrity.internal.m
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.data.model.budget.Procedure
import kr.sparta.tripmate.data.repository.BudgetRepositoryImpl
import kr.sparta.tripmate.databinding.FragmentBudgetDetailStatisticsBinding
import kr.sparta.tripmate.ui.viewmodel.budget.detail.statistics.BudgetStatisticsViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.detail.statistics.BudgetStatisticsViewModelFactory
import kr.sparta.tripmate.util.method.toMoneyFormat

private const val ARG_BUDGET_NUM = "budgetNum"

class BudgetDetailStatisticsFragment : Fragment() {

    companion object {
        private const val TAG = "BudgetDetailStatisticsF"
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
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "지출"


            setUsePercentValues(true)
            setEntryLabelColor(Color.BLACK)
            setTouchEnabled(false)
        }

        binding.budgetDetailExpenditureRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expenditureListAdapter
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

                val totalDataMap : Map<Int,Int> = mutableMapOf<Int, Int>().apply {
                    procedureGroupByCategoryNum.forEach { i, procedures ->
                        put(i,procedures.sumOf { it.money })
                    }
                }
                // 지출
                val totalExpenditureData : MutableMap<Int,Int> = mutableMapOf()
                // 수입
                val totalIncomeData : MutableMap<Int,Int> = mutableMapOf()

                totalDataMap.forEach { key, sum ->
                    Log.d(TAG, "initViewModels: key,$key sum,$sum")
                    when{
                        sum < 0 -> totalIncomeData[key] = -sum
                        sum > 0 -> totalExpenditureData[key] = sum
                    }
                }

                Log.d(TAG, "initViewModels:totalExpenditureData.size, ${totalExpenditureData.size}")
                Log.d(TAG, "initViewModels:totalIncomeData.size, ${totalIncomeData.size}")

                if (totalExpenditureData.isNotEmpty()){
                    val entries = ArrayList<PieEntry>()
                    val colorsItems = ArrayList<Int>()

                    val adapterPostItems = mutableListOf<Pair<Category,String>>()
                    val totlaExpenditureSum = totalExpenditureData.map { it.value }.sumOf { it }
                    totalExpenditureData.forEach { key, sum ->
                        entries.add(PieEntry(sum.toFloat(),categoryMap[key]?.name))
                        Log.d(TAG, "initViewModels: currentEntry ${entries.last().value} ${entries.last().label}")
                        colorsItems.add(Color.parseColor(categoryMap[key]?.color))

                        adapterPostItems.add(Pair(categoryMap[key]!!,"${(sum/totlaExpenditureSum.toFloat()*100).toInt()}%, ${sum.toMoneyFormat()}원"))
                    }
                    expenditureListAdapter.submitList(adapterPostItems)
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorsItems
                        valueTextColor = Color.BLACK
                        valueTextSize = 16f
                    }
                    val pieData = PieData(pieDataSet)
                    binding.budgetDetailExpenditurePiechart.data = pieData
                    binding.budgetDetailExpenditurePiechart.data.setValueFormatter(PercentFormatter())
                    binding.budgetDetailExpenditurePiechart.invalidate()
                }

                if (totalIncomeData.isNotEmpty()){
                    val entries = ArrayList<PieEntry>()
                    val colorsItems = ArrayList<Int>()

                    totalIncomeData.forEach { key, sum ->
                        entries.add(PieEntry(sum.toFloat(),categoryMap[key]?.name))
                        Log.d(TAG, "initViewModels: currentEntry ${entries.last().value} ${entries.last().label}")
                        colorsItems.add(Color.parseColor(categoryMap[key]?.color))
                    }


                    Log.d(TAG, "initViewModels:entries.size, ${entries.size}")
                    Log.d(TAG, "initViewModels:colorsItems.size, ${colorsItems.size}")

                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorsItems
                        valueTextColor = Color.BLACK
                        valueTextSize = 16f
                    }
                    val pieData = PieData(pieDataSet)
                   /* binding.budgetDetailSpendPiechart.data = pieData
                    binding.budgetDetailSpendPiechart.data.setValueFormatter(PercentFormatter())
                    binding.budgetDetailSpendPiechart.invalidate()*/
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}