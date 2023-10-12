package kr.sparta.tripmate.ui.mypage.scrap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.sparta.tripmate.databinding.FragmentScrapBinding
import kr.sparta.tripmate.databinding.FragmentScrapBookmarkBinding

class ScrapBookmarkFragment : Fragment() {
    companion object {
        fun newInstance(): ScrapBookmarkFragment = ScrapBookmarkFragment()
    }

    private var _binding : FragmentScrapBookmarkBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScrapBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

    }
}