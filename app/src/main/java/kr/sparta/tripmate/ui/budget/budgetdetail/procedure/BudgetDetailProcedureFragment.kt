package kr.sparta.tripmate.ui.budget.budgetdetail.procedure


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kr.sparta.tripmate.databinding.FragmentBudgetDetailProcedureBinding
import kr.sparta.tripmate.ui.budget.ProcedureDetailActivity
import kr.sparta.tripmate.ui.budget.budgetdetail.main.BudgetDetailActivity
import kr.sparta.tripmate.ui.viewmodel.budget.budgetdetail.statistics.BudgetStatisticsViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.budgetdetail.statistics.BudgetStatisticsFactory
import kr.sparta.tripmate.util.method.toMoneyFormat


/**
 * 작성자: 서정한
 * 내용: 가계부 세부 과정 Fragment
 * */


private const val ARG_BUDGET_NUM = "budgetNum"
class BudgetDetailProcedureFragment : Fragment() {
    companion object {
        fun newInstance(budgetNum: Int) = BudgetDetailProcedureFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_BUDGET_NUM, budgetNum)
            }
        }
    }

    private val budgetNum: Int by lazy {
        arguments?.getInt(ARG_BUDGET_NUM)!!
    }


    private lateinit var procedureContext: Context
    private lateinit var activity: BudgetDetailActivity

    private var _binding: FragmentBudgetDetailProcedureBinding? = null
    private val binding: FragmentBudgetDetailProcedureBinding
        get() = _binding!!

    private val adapter by lazy {
        BudgetDetailProcedureListAdapter(
            onItemClick = { procedureNum ->
                val intent = ProcedureDetailActivity.newIntent(
                    context = procedureContext,
                    budgetNum = activity.budget?.num ?: -1,
                    procedureNum = procedureNum,
                )
                startActivity(intent)
            }
        )
    }

    private val viewModel: BudgetStatisticsViewModel by activityViewModels {
        BudgetStatisticsFactory(budgetNum)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        procedureContext = context
        activity = context as BudgetDetailActivity
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
        initViewModel()
    }

    private fun initViews() = with(binding) {
        /**
         * 작성자: 서정한
         * 내용: 과정 데이터 init
         * */

        // RecyclerView
        budgetDetailProcedureRecyclerview.adapter = adapter
    }

    private fun initViewModel() {
        with(viewModel){
            budgetLiveData.observe(viewLifecycleOwner) {
                binding.budgetDetailStatusPrincipalTextview.text = it.money.toMoneyFormat() + "원"
                binding.budgetDetailStatusBalanceTextview.text = it.money.toMoneyFormat() + "원"
                binding.budgetDetailStatusDurationTextView.text = it.startDate + " ~ " + it.endDate
            }
            procedureList.observe(viewLifecycleOwner) { list ->
                adapter.submitList(list)

                // 잔액 업데이트
                if (list.isNotEmpty()) {
                    binding.budgetDetailStatusBalanceTextview.text = list.last().totalAmount.toMoneyFormat() + "원"
                }else{
                    binding.budgetDetailStatusBalanceTextview.text = binding.budgetDetailStatusPrincipalTextview.text.toString()
                }
            }

            budgetTotal.observe(viewLifecycleOwner){}
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}