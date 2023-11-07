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
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.repository.budget.BudgetRepositoryImpl
import kr.sparta.tripmate.databinding.FragmentHomeBinding
import kr.sparta.tripmate.ui.budget.detail.main.BudgetDetailActivity
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.ui.scrap.detail.ScrapDetailActivity
import kr.sparta.tripmate.ui.viewmodel.home.board.HomeBoardFactory
import kr.sparta.tripmate.ui.viewmodel.home.board.HomeBoardViewModel
import kr.sparta.tripmate.ui.viewmodel.home.budget.HomeBudgetFactory
import kr.sparta.tripmate.ui.viewmodel.home.budget.HomeBudgetViewModel
import kr.sparta.tripmate.ui.viewmodel.home.scrap.HomeBlogScrapFactory
import kr.sparta.tripmate.ui.viewmodel.home.scrap.HomeBlogScrapViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class HomeFragment : Fragment() {
    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private lateinit var homeContext: Context
    private lateinit var activity: MainActivity

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeScrapViewModel: HomeBlogScrapViewModel by viewModels {
        HomeBlogScrapFactory()
    }

    private val homeBoardViewModel: HomeBoardViewModel by viewModels {
        HomeBoardFactory()
    }
    private val homeBudgetViewModel: HomeBudgetViewModel by viewModels {
        HomeBudgetFactory(BudgetRepositoryImpl(homeContext))
    }
    private val homeBudgetListAdapter: HomeBudgetListAdapter by lazy {
        HomeBudgetListAdapter(
            onItemClicked = {
                startActivity(BudgetDetailActivity.newIntentForBudget(homeContext,it))
            }
        )
    }

    private val homeScrapListAdapter: HomeScrapListAdapter by lazy {
        HomeScrapListAdapter(
            onItemClick = { model ->
                val intent = ScrapDetailActivity.newIntentForScrap(
                    homeContext, model.copy(
                        isLike = true
                    )
                )
                homeResults.launch(intent)
            }
        )
    }

    private val homeBoardListAdapter: HomeBoardListAdapter by lazy {
        HomeBoardListAdapter(
            onItemClick = { model ->
                model.key?.let {
                    val intent = CommunityDetailActivity.newIntentForEntity(homeContext, model.key)
                    startActivity(intent)
                    // 조회수 업데이트
                    homeBoardViewModel.updateBoardView(model)
                }
            }
        )
    }

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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRoute()
        initView()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initView() = with(binding) {
        // 상단 프로필 이미지 && 닉네임
        val profileImg = SharedPreferences.getProfile(homeContext)
        homeProfileTitle.text = "${SharedPreferences.getNickName(homeContext)} 님"
        homeProfileImage.load(profileImg)

        // 블로그
        homeScrapRecyclerView.apply {
            layoutManager = GridLayoutManager(homeContext, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = homeScrapListAdapter
            setHasFixedSize(true)
        }

        // 인기글
        homeBoardRecyclerview.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = homeBoardListAdapter
            setHasFixedSize(true)
        }
        homeBudgetRecyclerview.apply {
            layoutManager = GridLayoutManager(homeContext, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = homeBudgetListAdapter
            setHasFixedSize(true)
        }

        val uid = SharedPreferences.getUid(homeContext)
        // 블로그 불러오기
        homeScrapViewModel.getAllBlogs(uid)

        // 인기글 불러오기
        homeBoardViewModel.getAllBoards()
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

    private fun initViewModel() {
        // 블로그
        with(homeScrapViewModel) {
            blogScraps.observe(viewLifecycleOwner) { list ->
                homeScrapListAdapter.submitList(list)
            }
        }

        // 인기글
        with(homeBoardViewModel) {
            boards.observe(viewLifecycleOwner) { it ->
                val sortedList = it?.let {
                    it.sortedByDescending { it1 -> it1?.likes }
                }.orEmpty()
                homeBoardListAdapter.submitList(sortedList)
            }
        }
        with(homeBudgetViewModel) {
            budgetLiveDataWhenBudgetChanged.observe(viewLifecycleOwner) {
                homeBudgetListAdapter.submitList(it)
            }
            budgetLiveDataWhenProcedureChanged.observe(viewLifecycleOwner) {
                homeBudgetListAdapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}