package kr.sparta.tripmate.ui.budget


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.sparta.tripmate.databinding.FragmentBudgetDetailStatusBinding


class BudgetDetailStatusFragment : Fragment() {
    private var _binding: FragmentBudgetDetailStatusBinding? = null
    private val binding: FragmentBudgetDetailStatusBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBudgetDetailStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {

    }

    companion object {
        fun newInstance() = BudgetDetailStatusFragment()
    }
}