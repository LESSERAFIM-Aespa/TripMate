package kr.sparta.tripmate.ui.budget.detail.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.sparta.tripmate.databinding.FragmentBudgetDetailStatisticsBinding

class BudgetDetailStatisticsFragment : Fragment() {
    companion object {
        fun newInstance() = BudgetDetailStatisticsFragment()
    }

    private var _binding: FragmentBudgetDetailStatisticsBinding? = null
    private val binding: FragmentBudgetDetailStatisticsBinding
        get() = _binding!!

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
    }

    private fun initViews() = with(binding) {

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}