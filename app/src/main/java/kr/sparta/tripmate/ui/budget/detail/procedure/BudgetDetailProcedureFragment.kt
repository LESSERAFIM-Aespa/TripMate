package kr.sparta.tripmate.ui.budget.detail.procedure


<<<<<<< HEAD
import android.content.Context
=======
>>>>>>> c8f940e ([Refector] : 가계부 상세페이지 폴더정리)
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import kr.sparta.tripmate.data.repository.BudgetRepositoryImpl
import kr.sparta.tripmate.databinding.FragmentBudgetDetailProcedureBinding
import kr.sparta.tripmate.ui.budget.detail.main.BudgetDetailActivity
import kr.sparta.tripmate.ui.viewmodel.budget.detail.procedure.BudgetProcedureFactory
import kr.sparta.tripmate.ui.viewmodel.budget.detail.procedure.BudgetProcedureViewModel
import kr.sparta.tripmate.util.method.setCommaForMoneeyText
=======
import kr.sparta.tripmate.databinding.FragmentBudgetDetailProcedureBinding
>>>>>>> c8f940e ([Refector] : 가계부 상세페이지 폴더정리)


/**
 * 작성자: 서정한
 * 내용: 가계부 세부 과정 Fragment
 * */
class BudgetDetailProcedureFragment : Fragment() {
    companion object {
        fun newInstance() = BudgetDetailProcedureFragment()
    }

<<<<<<< HEAD
    private lateinit var procedureContext: Context
    private lateinit var activity: BudgetDetailActivity

=======
>>>>>>> c8f940e ([Refector] : 가계부 상세페이지 폴더정리)
    private var _binding: FragmentBudgetDetailProcedureBinding? = null
    private val binding: FragmentBudgetDetailProcedureBinding
        get() = _binding!!

    private val adapter by lazy {
        BudgetDetailProcedureListAdapter()
    }

<<<<<<< HEAD
    private val viewModel: BudgetProcedureViewModel by viewModels() {
        BudgetProcedureFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        procedureContext = context
        activity = context as BudgetDetailActivity
    }

=======
>>>>>>> c8f940e ([Refector] : 가계부 상세페이지 폴더정리)
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
<<<<<<< HEAD
        initViewModel()
    }

    private fun initViews() = with(binding) {
        /**
         * 작성자: 서정한
         * 내용: 과정 데이터 init
         * */
        fun initProcedureData() {

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
                binding.budgetDetailStatusBalanceTextview.text = setCommaForMoneeyText(list.last().totalAmount.toString())
            }
        }
    }


=======
    }

    private fun initViews() = with(binding) {
        budgetDetailProcedureRecyclerview.adapter = adapter
    }

>>>>>>> c8f940e ([Refector] : 가계부 상세페이지 폴더정리)
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}