package kr.sparta.tripmate.ui.budget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentBudgetListBinding


/**
 * 작성자 : 김성환
 * 가계부(가계부 관리) 프래그먼트
 * */
class BudgetListFragment : Fragment() {
    private var _binding : FragmentBudgetListBinding? = null
    private val binding : FragmentBudgetListBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBudgetListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding){

    }

    companion object {
        fun newInstance() = BudgetListFragment()
    }
}