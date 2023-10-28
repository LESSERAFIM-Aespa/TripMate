package kr.sparta.tripmate.ui.search

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
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.databinding.FragmentScrapBinding
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.ui.viewmodel.searchblog.main.SearchBlogFactory
import kr.sparta.tripmate.ui.viewmodel.searchblog.main.SearchBlogViewModel
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class ScrapFragment : Fragment() {
    companion object {
        fun newInstance(): ScrapFragment = ScrapFragment()
    }

    private lateinit var scrapContext: Context

    private var _binding: FragmentScrapBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        SearchBlogAdapter(
            onItemClick = { model, position ->
                val intent = SearchBlogDetailActivity.newIntentForScrap(requireContext(), model)
                scrapResults.launch(intent)
            },

            onLikeClick = { model, position ->
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.updateIsLike(
                        model = model.copy(
                            isLike = !model.isLike
                        ),
                    )
                }

                //위에서 model의 isLike값을 반전시키고 뷰모델에서 업데이트되기때문에 아래의 model.isLike는 값이 반전되기 이전이다.
                if (model.isLike) {
                    removeScrapFirebase(model)
                } else saveScrapFirebase(model)
            }
        )
    }

    private val scrapResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
            }
        }

    private val viewModel: SearchBlogViewModel by viewModels { SearchBlogFactory() }

    private var searchQuery: String? = null

    // 화면에 표시되 검색결과 item 수
    private var searchResultDisplayCount: Int = 10

    override fun onAttach(context: Context) {
        super.onAttach(context)
        scrapContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScrapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        scrapRecyclerView.adapter = adapter
        scrapRecyclerView.layoutManager = GridLayoutManager(scrapContext, 2)

        // 무한스크롤
        scrapRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (searchResultDisplayCount in 0..100) {
                        if (viewModel.isLoading.value!! && !recyclerView.canScrollVertically(1)) {
                            if (searchQuery.isNullOrEmpty()) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    Log.d("TripMates", "currentDisplay : $searchResultDisplayCount")
                                    searchResultDisplayCount += 10
                                    viewModel.searchAPIResult(searchQuery!!, scrapContext)
                                }
                            }
                        }
                    } else {
                        scrapContext.shortToast("한 번에 표시할 검색 결과 개수는 100개입니다.")
                    }
                }
            }
        )
        scrapRecyclerView.setHasFixedSize(true)

        // 검색
        scrapSearchView.isSubmitButtonEnabled = true
        scrapSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchQuery = it.trim()
                    Log.d("TripMates", "검색어 : $searchQuery")

                    searchQuery?.let {
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.searchAPIResult(searchQuery!!, requireContext())
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initViewModel() = with(viewModel) {
        searchBlogs.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.scrapLoading.visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun saveScrapFirebase(model: SearchBlogEntity) {
        CoroutineScope(Dispatchers.Main).launch {
            val uid = SharedPreferences.getUid(scrapContext)
            viewModel.saveScrap(uid, model)
        }
    }


    private fun removeScrapFirebase(model: SearchBlogEntity) {
        CoroutineScope(Dispatchers.Main).launch {
            val uid = SharedPreferences.getUid(scrapContext)
            viewModel.removeScrap(uid, model)
        }
    }
}