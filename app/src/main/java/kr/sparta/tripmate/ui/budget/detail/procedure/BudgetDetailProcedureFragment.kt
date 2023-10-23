package kr.sparta.tripmate.ui.budget.detail.procedure


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.sparta.tripmate.databinding.FragmentBudgetDetailProcedureBinding


/**
 * 작성자: 서정한
 * 내용: 가계부 세부 과정 Fragment
 * */
class BudgetDetailProcedureFragment : Fragment() {
    companion object {
        fun newInstance() = BudgetDetailProcedureFragment()
    }

    private var _binding: FragmentBudgetDetailProcedureBinding? = null
    private val binding: FragmentBudgetDetailProcedureBinding
        get() = _binding!!

    private val adapter by lazy {
        BudgetDetailProcedureListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBudgetDetailProcedureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        budgetDetailProcedureRecyclerview.adapter = adapter
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}