package kr.sparta.tripmate.ui.budget.detail.procedure


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import kr.sparta.tripmate.databinding.FragmentBudgetDetailProcedureBinding
import kr.sparta.tripmate.ui.budget.detail.main.BudgetDetailActivity
import kr.sparta.tripmate.ui.viewmodel.budget.procedure.BudgetProcedureFactory
import kr.sparta.tripmate.ui.viewmodel.budget.procedure.BudgetProcedureViewModel


/**
 * 작성자: 서정한
 * 내용: 가계부 세부 과정 Fragment
 * */
class BudgetDetailProcedureFragment : Fragment() {
    companion object {
        fun newInstance() = BudgetDetailProcedureFragment()
    }

    private lateinit var procedureContext: Context
    private lateinit var activity: BudgetDetailActivity

    private var _binding: FragmentBudgetDetailProcedureBinding? = null
    private val binding: FragmentBudgetDetailProcedureBinding
        get() = _binding!!

    private val adapter by lazy {
        BudgetDetailProcedureListAdapter()
    }

    private val viewModel: BudgetProcedureViewModel by viewModels() {
        BudgetProcedureFactory()
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
        fun initProcedureData() {
            /**
             * 작성자: 서정한
             * 내용: 금액에 콤마 추가
             * */
            fun setCommaForMoneeyText(money: String): String {
                val commaCount = if (money.length % 3 == 0) {
                    (money.length / 3) - 1
                } else {
                    money.length / 3
                }
                var commaStartIndex = money.length % 3
                val comma = ','
                val stringBuilder = StringBuilder()
                stringBuilder.append(money)

                for (i in 0 until commaCount) {
                    stringBuilder.insert(commaStartIndex, comma)
                    commaStartIndex += 4
                }

                return "${stringBuilder}원"
            }

            activity.budget?.let {
                // 원금
                budgetDetailStatusPrincipalTextview.text = setCommaForMoneeyText(it.money.toString())
                // 잔액
                budgetDetailStatusBalanceTextview.text = setCommaForMoneeyText(it.money.toString())
                // 과정 RecyclerView item
                viewModel.getAllProcedures(it)
            }
        }

        // RecyclerView
        budgetDetailProcedureRecyclerview.adapter = adapter

        initProcedureData()
    }

    private fun initViewModel() {
        viewModel.procedureList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)

            // 잔액 업데이트
            if (list.isNotEmpty()) {
                binding.budgetDetailStatusBalanceTextview.text = list.last().totalAmount.toString()
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}