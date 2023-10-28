package kr.sparta.tripmate.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentHomeBinding
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.ui.scrap.ScrapDetail
import kr.sparta.tripmate.ui.viewmodel.home.board.HomeBoardFactory
import kr.sparta.tripmate.ui.viewmodel.home.board.HomeBoardViewModel
import kr.sparta.tripmate.ui.viewmodel.home.scrap.HomeScrapFactory
import kr.sparta.tripmate.ui.viewmodel.home.scrap.HomeScrapViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class HomeFragment : Fragment() {
    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private lateinit var homeContext: Context
    private lateinit var activity: MainActivity

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val homeScrapViewModel: HomeScrapViewModel by viewModels {
        HomeScrapFactory()
    }
    private val homeBoardViewModel: HomeBoardViewModel by viewModels {
        HomeBoardFactory()
    }

    private lateinit var homeScrapListAdapter: HomeScrapListAdapter
    private lateinit var homeBoardListAdapter: HomeBoardListAdapter

    private val homeResults = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeContext = context
        activity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inputToolBar()
        // 페이지 이동 정의
        initRoute()

        homeView()
        boardView()

        return binding.root
    }

    private fun boardView() {
        homeBoardListAdapter = HomeBoardListAdapter(
            onItemClick = { model, position ->
                val intent = CommunityDetailActivity.newIntentForEntity(homeContext, model)
                startActivity(intent)
                // 조회수 업데이트
                homeBoardViewModel.viewHomeBoardData(model, position)
            }
        )
        binding.homeRecyclerView2.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            adapter = homeBoardListAdapter
            setHasFixedSize(true)
        }
    }

    /**
     * 작성자: 서정한
     * 내용: Home에서 다른 Fragment로 이동하는 로직
     * */
    private fun initRoute() = with(binding) {
        // 프로필
        homeProfileImage.setOnClickListener {
            activity.moveTabFragment(R.string.main_tab_title_mypage)
        }

        // 스크랩
        homeArrow1.setOnClickListener {
            activity.moveTabFragment(R.string.main_tab_title_blog)
        }

        // 커뮤니티
        homeArrow2.setOnClickListener {
            activity.moveTabFragment(R.string.main_tab_title_community)
        }

        // 가계부
        homeArrow3.setOnClickListener {
            activity.moveTabFragment(R.string.main_tab_title_budget)
        }

    }

    private fun homeView() {
        homeScrapListAdapter = HomeScrapListAdapter(
            onItemClick = { model, position ->
                model.isLike = true
                val intent = ScrapDetail.newIntentForScrap(homeContext, model)
                intent.putExtra("Data", model)
                homeResults.launch(intent)
            }
        )
        binding.homeRecyclerView1.apply {
            layoutManager = GridLayoutManager(homeContext, 2, GridLayoutManager.HORIZONTAL, false)
            adapter = homeScrapListAdapter
            setHasFixedSize(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        val uid = SharedPreferences.getUid(homeContext)
        homeScrapViewModel.updateScrapData(homeContext)
        homeBoardViewModel.getHomeBoardData(uid)
    }

    private fun observeViewModel() {
        with(homeScrapViewModel) {
            homeScraps.observe(viewLifecycleOwner) { list ->
                homeScrapListAdapter.submitList(list)
            }
        }
        with(homeBoardViewModel) {
            homeBoard.observe(viewLifecycleOwner) {
                val sortedList = it.sortedByDescending { it?.likes }
                Log.d("TripMates", "좋아요순 정렬된 데이터 :${sortedList}")
                homeBoardListAdapter.submitList(sortedList)
            }
        }
    }

    private fun inputToolBar() {
        binding.homeProfileTitle.text = "${SharedPreferences.getNickName(homeContext)} 님"
        binding.homeProfileImage.load(
            SharedPreferences.getProfile(homeContext)
        )
    }
}