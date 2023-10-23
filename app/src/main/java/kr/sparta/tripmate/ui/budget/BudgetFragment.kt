package kr.sparta.tripmate.ui.budget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.repository.BudgetRepositoryImpl
import kr.sparta.tripmate.databinding.FragmentBudgetBinding
import kr.sparta.tripmate.domain.repository.BudgetRepository
import kr.sparta.tripmate.ui.viewmodel.budget.BudgetViewModel
import kr.sparta.tripmate.ui.viewmodel.budget.BudgetViewModelFactory

class BudgetFragment : Fragment() {
    companion object {
        fun newInstance() = BudgetFragment()
    }


    private var _binding: FragmentBudgetBinding? = null
    private val binding: FragmentBudgetBinding
        get() = _binding!!

    private val budgetAdapter by lazy {
        BudgetAdapter(object : BudgetAdapter.BudgetListEventListener {
            override fun itemClicked(num: Int) {
                startActivity(BudgetDetailActivity.newIntent(this@BudgetFragment.requireContext(),num))
                /*startActivity(
                    BudgetContentActivity.newIntentForEdit(
                        this@BudgetFragment.requireContext(),
                        num
                    )
                )*/
            }
        })
    }

    private val budgetViewModel: BudgetViewModel by viewModels {
        BudgetViewModelFactory(BudgetRepositoryImpl(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModels()
    }


    private fun initViews() = with(binding) {
        budgetListFloatingactionbutton.setOnClickListener {
            startActivity(BudgetContentActivity.newIntentForAdd(requireContext()))
        }

        budgetListRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = budgetAdapter
        }
    }

    private fun initViewModels() {
        with(budgetViewModel) {
            budgetLiveDataWhenBudgetChanged.observe(viewLifecycleOwner) {
                budgetAdapter.submitList(it)
            }
            budgetLiveDataWhenProcedureChanged.observe(viewLifecycleOwner) {
                budgetAdapter.submitList(it)
            }
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}
