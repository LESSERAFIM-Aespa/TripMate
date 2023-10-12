package kr.sparta.tripmate.ui.mypage.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import kr.sparta.tripmate.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    companion object {
        fun newInstance() : MyPageFragment = MyPageFragment()
    }
    private var _binding : FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val adapter: MyPageTabLayoutAdapter by lazy{
        MyPageTabLayoutAdapter(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView()=with(binding) {
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.setText(adapter.getTitle(position))
        }.attach()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}