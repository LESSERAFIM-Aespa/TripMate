package kr.sparta.tripmate.ui.budget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentBudgetBinding

class BudgetFragment : Fragment() {
    companion object {
        fun newInstance() = BudgetFragment()
    }


    private var _binding: FragmentBudgetBinding? = null
    private val binding: FragmentBudgetBinding
        get() = _binding!!

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
    }

    private fun initViews() = with(binding) {
        budgetListFloatingactionbutton.setOnClickListener {
            startActivity(BudgetContentActivity.newIntentForAdd(requireContext()))
        }

        //test
        // 이동연결확립후 삭제할거
        textView.setOnClickListener {
            startActivity(BudgetDetailActivity.newIntent(requireContext(),1))
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}