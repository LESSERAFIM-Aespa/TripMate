package kr.sparta.tripmate.ui.scrap.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kr.sparta.tripmate.databinding.FragmentScrapBinding
import kr.sparta.tripmate.ui.scrap.detail.ScrapDetailActivity
import kr.sparta.tripmate.ui.viewmodel.scrap.main.SearchBlogFactory
import kr.sparta.tripmate.ui.viewmodel.scrap.main.SearchBlogViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences
import java.util.Random

class ScrapFragment : Fragment() {
    companion object {
        fun newInstance(): ScrapFragment = ScrapFragment()
    }

    var searchLoading = false
    private lateinit var scrapContext: Context
    private val binding by lazy { FragmentScrapBinding.inflate(layoutInflater) }

    private lateinit var scrapAdapter: ScrapAdapter

    private val scrapResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
            }
        }

    private val viewModel: SearchBlogViewModel by viewModels { SearchBlogFactory() }

    var searchQuery: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        scrapContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setUpView()
        searchView()
        initImage()

        return binding.root
    }

    private fun initImage() {
        val suggestionItems: ArrayList<String> = arrayListOf("울산 여행지", "부산 여행지","인천 여행지", "대구 여행지", "서울 여행지", "대전 여행지", "광주 여행지", "세종 여행지" )
        val random = Random()
        val randomIndex = random.nextInt(suggestionItems.size)
        val randomItem = suggestionItems[randomIndex]
        scrapViewModel.searchImageResult(randomItem)
        binding.scrapImagesearchTextview.text = randomItem + "로 떠나볼까요?"
        binding.scrapImagelayout.setOnClickListener {
            scrapViewModel.searchAPIResult(randomItem, scrapContext)
            binding.scrapRecyclerView.visibility=View.VISIBLE
            binding.scrapImagelayout.visibility= View.GONE
        }
    }
    override fun onPause() {
        super.onPause()
        scrapViewModel.resetList()
        searchQuery = null
        binding.scrapRecyclerView.visibility=View.GONE
        binding.scrapImagelayout.visibility= View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() = with(viewModel) {
        searchList.observe(viewLifecycleOwner) {
            scrapAdapter.submitList(it)

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.scrapLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun searchView() {
        scrapAdapter = ScrapAdapter(
            onItemClick = { model ->
                val intent = ScrapDetailActivity.newIntentForScrap(scrapContext, model)
                scrapResults.launch(intent)
            },
            onLikeClick = { model, position ->
                // 사용자의 검색결과에서 보이는 좋아요 업데이트
                viewModel.updateIsLike(
                    model = model.copy(
                        isLike = !model.isLike
                    ),
                    position
                )

                val uid = SharedPreferences.getUid(scrapContext)
                // 블로그 스크랩목록 업데이트
                viewModel.updateBlogScrap(uid, model)
            }
        )

        binding.scrapRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = scrapAdapter
            binding.scrapRecyclerView.addOnScrollListener(
                SearchScrollListener(
                    viewModel,
                    this@ScrapFragment, scrapContext
                )
            )
            setHasFixedSize(true)
        }
    }

    private fun setUpView() {
        binding.scrapSearchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        searchQuery = it.trim()
                        Log.d("TripMates", "검색어 : $searchQuery")
                        setupListeners()
                        searchLoading = !searchLoading
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }

    private fun setupListeners() {
        searchQuery?.let {
            viewModel.searchAPIResult(searchQuery!!, requireContext())
        }
    }
}