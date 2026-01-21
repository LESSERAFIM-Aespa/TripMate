package kr.sparta.tripmate.ui.budget.budgetdetail.statistics

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
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
import kr.sparta.tripmate.ui.viewmodel.budget.budgetdetail.statistics.BudgetStatisticsViewModel
import kr.sparta.tripmate.util.method.setMaxLength
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.method.toMoneyFormat
import java.io.File
import java.io.FileOutputStream
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

    private val viewModel: BudgetStatisticsViewModel by activityViewModels()

    private val expenditureListAdapter: BudgetDetailStatisticsListAdapter =
        BudgetDetailStatisticsListAdapter()
    private val incomeListAdapter: BudgetDetailStatisticsListAdapter =
        BudgetDetailStatisticsListAdapter()

    fun PieDataSet.toCustomFormat() = this.apply {
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
        binding.budgetStatisticsCalculateEdittext.setMaxLength(3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModels()
    }


    private fun initViews() = with(binding) {
        binding.budgetDetailExpenditurePiechart.apply {
            renderer = CustomPieChartRenderer(this, 0f)
            setExtraOffsets(35f, 8f, 35f, 8f)

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
            setExtraOffsets(35f, 8f, 35f, 8f)

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
            budgetFlowToLiveData.observe(viewLifecycleOwner) {
                binding.budgetStatisticsMoneyTextview.text = it.money.toMoneyFormat() + "원"
                binding.budgetStatisticsRemainTextview.text = it.money.toMoneyFormat() + "원"
                binding.budgetStatisticsDurationTextview.text =
                    it.startDate + " ~ " + it.endDate
            }

            budgetTotal.observe(viewLifecycleOwner) { budgetTotal ->
                val budget: Budget = budgetTotal.first
                val categories: List<Category> = budgetTotal.second
                val categoryMap: Map<Int, Category> =
                    mutableMapOf<Int, Category>().apply { categories.forEach { put(it.num, it) } }

                val procedures: List<Procedure> = budgetTotal.third
                val procedureGroupByCategoryNum: Map<Int, List<Procedure>> =
                    procedures.groupBy { it.categoryNum }

                // 지출
                val totalExpenditureData: MutableMap<Int, Int> = mutableMapOf()
                // 수입
                val totalIncomeData: MutableMap<Int, Int> = mutableMapOf()

                procedureGroupByCategoryNum.forEach { i, procedures ->
                    var expenditure = 0
                    var income = 0

                    procedures.forEach { procedure ->
                        if (procedure.money < 0) {//수입
                            income += -procedure.money
                        } else if (procedure.money > 0) {//지출
                            expenditure += procedure.money
                        }
                    }

                    if (expenditure != 0) totalExpenditureData[i] = expenditure
                    if (income != 0) totalIncomeData[i] = income
                }

                val totalExpenditureSum = totalExpenditureData.map { it.value }.sumOf { it }
                val totalIncomeSum = totalIncomeData.map { it.value }.sumOf { it }

                if (totalExpenditureData.isNotEmpty()) {
                    binding.budgetDetailExpenditurePiechart.visibility = View.VISIBLE
                    binding.budgetDetailExpenditureRecyclerview.visibility = View.VISIBLE
                    binding.expenditureTitleTextview.visibility = View.VISIBLE
                    binding.expenditureTextview.visibility = View.VISIBLE

                    val entries = ArrayList<PieEntry>()
                    val colorsItems = ArrayList<Int>()

                    val adapterPostItems = mutableListOf<Pair<Category, String>>()
                    totalExpenditureData.toList().sortedByDescending { it.second }
                        .forEach { (key, sum) ->
                            entries.add(PieEntry(sum.toFloat(), ""))
                            Log.d(
                                TAG,
                                "initViewModels: currentEntry ${entries.last().value} ${entries.last().label}"
                            )
                            colorsItems.add(Color.parseColor(categoryMap[key]?.color))

                            adapterPostItems.add(
                                Pair(
                                    categoryMap[key]!!,
                                    "${
                                        String.format(
                                            "%.1f",
                                            sum / totalExpenditureSum.toFloat() * 100
                                        )
                                    }%, ${sum.toMoneyFormat()}원"
                                )
                            )
                        }

                    expenditureListAdapter.submitList(adapterPostItems)

                    val pieDataSet = PieDataSet(entries, "").apply {
                        colors = colorsItems
                        setValueTextColors(colorsItems)
                    }
                    //.toCustomFormat()

                    val pieData = PieData(pieDataSet)
                    binding.budgetDetailExpenditurePiechart.data = pieData
                    binding.budgetDetailExpenditurePiechart.data.setValueFormatter(PercentFormatter())
                    binding.budgetDetailExpenditurePiechart.animate()
                    binding.budgetDetailExpenditurePiechart.invalidate()
                    binding.expenditureTextview.text = totalExpenditureSum.toMoneyFormat() + "원"
                } else {
                    binding.budgetDetailExpenditurePiechart.visibility = View.GONE
                    binding.budgetDetailExpenditureRecyclerview.visibility = View.GONE
                    binding.expenditureTitleTextview.visibility = View.GONE
                    binding.expenditureTextview.visibility = View.GONE
                }

                if (totalIncomeData.isNotEmpty()) {
                    binding.budgetDetailIncomePiechart.visibility = View.VISIBLE
                    binding.budgetDetailIncomeRecyclerview.visibility = View.VISIBLE
                    binding.incomeTitleTextview.visibility = View.VISIBLE
                    binding.incomeTextview.visibility = View.VISIBLE

                    val entries = ArrayList<PieEntry>()
                    val colorsItems = ArrayList<Int>()

                    val adapterPostItems = mutableListOf<Pair<Category, String>>()
                    totalIncomeData.toList().sortedByDescending { it.second }
                        .forEach { (key, sum) ->
                            entries.add(PieEntry(sum.toFloat(), ""))
                            Log.d(
                                TAG,
                                "initViewModels: currentEntry ${entries.last().value} ${entries.last().label}"
                            )
                            colorsItems.add(Color.parseColor(categoryMap[key]?.color))
                            Log.d(TAG, "test: $sum")
                            Log.d(TAG, "test: $totalIncomeSum")

                            Log.d(TAG, "test: ${sum / totalIncomeSum.toFloat() * 100}")
                            adapterPostItems.add(
                                Pair(
                                    categoryMap[key]!!,
                                    "${
                                        String.format(
                                            "%.1f",
                                            sum / totalIncomeSum.toFloat() * 100
                                        )
                                    }%, ${sum.toMoneyFormat()}원"
                                )
                            )
                        }

                    incomeListAdapter.submitList(adapterPostItems)

                    val pieDataSet = PieDataSet(entries, "").apply {
                        colors = colorsItems
                        setValueTextColors(colorsItems)
                    }
                    //.toCustomFormat()

                    val pieData = PieData(pieDataSet)
                    binding.budgetDetailIncomePiechart.data = pieData
                    binding.budgetDetailIncomePiechart.data.setValueFormatter(PercentFormatter())

                    binding.budgetDetailIncomePiechart.animate()
                    binding.budgetDetailIncomePiechart.invalidate()

                    binding.incomeTextview.text = totalIncomeSum.toMoneyFormat() + "원"
                } else {
                    binding.budgetDetailIncomePiechart.visibility = View.GONE
                    binding.budgetDetailIncomeRecyclerview.visibility = View.GONE
                    binding.incomeTitleTextview.visibility = View.GONE
                    binding.incomeTextview.visibility = View.GONE
                }

                binding.budgetStatisticsDurationTextview.text =
                    budget.startDate + " ~ " + budget.endDate
                binding.budgetStatisticsMoneyTextview.text = budget.money.toMoneyFormat() + "원"
                binding.budgetStatisticsExpenditureTextview.text =
                    totalExpenditureSum.toMoneyFormat() + "원"
                binding.budgetStatisticsIncomeTextview.text = totalIncomeSum.toMoneyFormat() + "원"
                binding.budgetStatisticsRemainTextview.text =
                    (budget.money - totalExpenditureSum + totalIncomeSum).toMoneyFormat() + "원"

                binding.budgetStatisticsCalculateTextview.text =
                    totalExpenditureSum.toMoneyFormat() + "원"

                binding.budgetStatisticsCalculateButton.setOnClickListener {
                    if (binding.budgetStatisticsCalculateEdittext.text.toString()
                            .any { it !in '0'..'9' }
                    ) {
                        context?.shortToast(" 숫자만 입력해주세요.")
                    } else if (binding.budgetStatisticsCalculateEdittext.text.toString()
                            .toInt() in 2..100
                    ) {
                        val n =
                            binding.budgetStatisticsCalculateEdittext.text.toString().toInt()
                        binding.budgetStatisticsCalculateTextview.text =
                            (totalExpenditureSum / n).toMoneyFormat() + "원"
                    } else {
                        context?.shortToast(" 2 ~ 100명까지 입력할 수 있습니다.")
                    }
                }

                binding.budgetStatisticsShareImageview.setOnClickListener {
                    // getExternalFilesDir() 사용으로 권한 불필요
                    captureScrollableContent()
                }
            }
        }
    }

    private fun captureScrollableContent() {
        val scrollView = binding.budgetStatisticsScrollview
        val bitmap = Bitmap.createBitmap(
            scrollView.width, scrollView.getChildAt(0).height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)

        saveAndShareImage(bitmap)
    }

    private fun saveAndShareImage(bitmap: Bitmap) {
        val file = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "scroll_capture.png"
        )
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()

        val uri =
            FileProvider.getUriForFile(this.requireContext(), "kr.sparta.tripmate.provider", file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
        }

        startActivity(Intent.createChooser(shareIntent, "스크롤 캡쳐 공유"))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}