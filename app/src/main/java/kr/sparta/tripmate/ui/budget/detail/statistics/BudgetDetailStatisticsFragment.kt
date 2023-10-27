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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.github.mikephil.charting.utils.MPPointF
import kr.sparta.tripmate.data.repository.BudgetRepositoryImpl
import kr.sparta.tripmate.databinding.FragmentBudgetDetailStatisticsBinding
import kr.sparta.tripmate.ui.viewmodel.budget.detail.statistics.BudgetStatisticsViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.detail.statistics.BudgetStatisticsViewModelFactory

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

    }

    private fun initViewModels() {
        with(viewModel){
            budgetTotal.observe(viewLifecycleOwner){ budgetTotal ->
                val budget = budgetTotal.first
                val categories = budgetTotal.second
                val procedueres = budgetTotal.third

                Log.d(TAG, "initViewModels:  $budget")
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}