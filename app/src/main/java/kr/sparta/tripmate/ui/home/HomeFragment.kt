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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentHomeBinding
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.community.main.CommunityFragment
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.ui.search.SearchBlogDetailActivity
import kr.sparta.tripmate.ui.viewmodel.home.HomeFactory
import kr.sparta.tripmate.ui.viewmodel.home.HomeViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class HomeFragment : Fragment() {
    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private lateinit var homeContext: Context
    private lateinit var activity: MainActivity

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    private val viewModel: HomeViewModel by viewModels {
        HomeFactory()
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
                CoroutineScope(Dispatchers.Main).launch {
                    // 조회수 업데이트
                    viewModel.updatesBoardViews(model)

                    val intent = CommunityDetailActivity.newIntentForEntity(
                        homeContext,
                        model = model.copy(
                            views = (model.views?.let { Integer.parseInt(it) + 1}).toString()
                        )
                    )
                    homeResults.launch(intent)
                }
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
            (activity.getTabFragment(1) as CommunityFragment).getAllBoards()
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
                val intent = SearchBlogDetailActivity.newIntentForScrap(homeContext, model)
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

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAllScraps(homeContext)
            viewModel.getAllBoardsOrderByLikes()
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            scraps.observe(viewLifecycleOwner) {list ->
                homeScrapListAdapter.submitList(list)
            }

            boards.observe(viewLifecycleOwner) {list ->
                homeBoardListAdapter.submitList(list)
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