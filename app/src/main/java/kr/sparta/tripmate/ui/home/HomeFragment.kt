package kr.sparta.tripmate.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentHomeBinding
import kr.sparta.tripmate.ui.budget.BudgetFragment
import kr.sparta.tripmate.ui.community.main.CommunityFragment
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.ui.mypage.home.MyPageFragment
import kr.sparta.tripmate.ui.scrap.ScrapDetail
import kr.sparta.tripmate.ui.scrap.ScrapFragment
import kr.sparta.tripmate.ui.viewmodel.home.FirstViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class HomeFragment : Fragment() {
    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val firstViewModel: FirstViewModel by viewModels()
    private lateinit var homeFirstAdapter: HomeFirstAdapter

    private val homeResults = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK){}
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inputToolBar()
        // 페이지 이동 정의
        initRoute()

        homeView()

        return binding.root
    }

    /**
     * 작성자: 서정한
     * 내용: Home에서 다른 Fragment로 이동하는 로직
     * */
    private fun initRoute()=with(binding) {
        val mainActivity = requireActivity() as MainActivity

        // 프로필
        homeProfileImage.setOnClickListener {
            mainActivity.moveTabFragment(R.string.main_tab_title_mypage)
        }

        // 스크랩
        homeArrow1.setOnClickListener {
            mainActivity.moveTabFragment(R.string.main_tab_title_scrap)
        }

        // 커뮤니티
        homeArrow2.setOnClickListener {
            mainActivity.moveTabFragment(R.string.main_tab_title_community)
        }

        // 가계부
        homeArrow3.setOnClickListener {
            mainActivity.moveTabFragment(R.string.main_tab_title_budget)
        }

    }

    private fun homeView() {
        homeFirstAdapter = HomeFirstAdapter(
            onItemClick = { model, position ->
                val intent = ScrapDetail.newIntentForScrap(requireContext(), model)
                homeResults.launch(intent)
            }
        )
        binding.homeRecyclerView1.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = homeFirstAdapter
            setHasFixedSize(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        firstViewModel.apply {
            getFirstData(SharedPreferences.getUid(requireContext())).observe(viewLifecycleOwner) {
                homeFirstAdapter.submitList(it)
            }
        }
    }

    private fun inputToolBar() {
        binding.homeProfileTitle.text = "${SharedPreferences.getNickName(requireContext())} 님"
        Glide.with(requireContext())
            .load(SharedPreferences.getProfile(requireContext()))
            .into(binding.homeProfileImage)
    }
}