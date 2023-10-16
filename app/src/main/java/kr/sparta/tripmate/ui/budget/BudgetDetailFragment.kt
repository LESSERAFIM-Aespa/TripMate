package kr.sparta.tripmate.ui.budget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentBudgetDetailBinding
import kr.sparta.tripmate.databinding.FragmentBudgetDetailStatusBinding
import kr.sparta.tripmate.databinding.FragmentBudgetListBinding


class BudgetDetailFragment : Fragment() {
    private var _binding: FragmentBudgetDetailBinding? = null
    private val binding: FragmentBudgetDetailBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBudgetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {

    }

    companion object {
        fun newInstance() = BudgetDetailFragment()
    }
}