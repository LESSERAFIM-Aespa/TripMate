package kr.sparta.tripmate.ui.scrap.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentScrapBinding
import kr.sparta.tripmate.ui.scrap.detail.ScrapDetailActivity
import kr.sparta.tripmate.ui.viewmodel.scrap.main.SearchBlogFactory
import kr.sparta.tripmate.ui.viewmodel.scrap.main.SearchBlogViewModel
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences
import java.util.Random

class ScrapFragment : Fragment() {
    companion object {
        fun newInstance(): ScrapFragment = ScrapFragment()
    }

    var searchLoading = false
    private lateinit var scrapContext: Context

    private val binding by lazy { FragmentScrapBinding.inflate(layoutInflater) }

    private val scrapAdapter by lazy {
        ScrapAdapter(
            onItemClick = { model ->
                val intent = ScrapDetailActivity.newIntentForScrap(scrapContext, model)
                scrapResults.launch(intent)
            },
            onLikeClick = { model, position ->
                viewModel.updateIsLike(
                    model = model.copy(
                        isLike = !model.isLike
                    ),
                    position
                )

                val uid = SharedPreferences.getUid(scrapContext)
                viewModel.updateBlogScrap(uid, model)
            }
        )
    }

    private val scrapResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
            }
        }

    private val viewModel: SearchBlogViewModel by viewModels { SearchBlogFactory() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        scrapContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        setRandomRecommandView()
    }

    private fun initView() = with(binding) {
        // RecyclerView
        scrapRecyclerView.layoutManager = GridLayoutManager(context, 2)
        scrapRecyclerView.adapter = scrapAdapter

//        scrapRecyclerView.addOnScrollListener(
//            object: RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//
//                    if (SearchBlogViewModel.currentDisplay in 0..100) {
//                        if (searchLoading && !recyclerView.canScrollVertically(1)) {
//                            val query = scrapSearchView.query
//                            if (query.isNullOrEmpty()) {
//                                Log.d("TripMates", "currentDisplay : ${SearchBlogViewModel.currentDisplay}")
//                                SearchBlogViewModel.currentDisplay += 10
//                                viewModel.searchAPIResult(query.toString(), scrapContext)
//                            }
//                        }
//                    } else scrapContext.shortToast("한 번에 표시할 검색 결과 개수는 100개입니다.")
//                }
//            }
//        )
        scrapRecyclerView.setHasFixedSize(true)

        // SearchView
        scrapSearchView.isSubmitButtonEnabled = true
        scrapSearchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.searchAPIResult(query.trim(), scrapContext)
                        searchLoading = !searchLoading
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText == "") {
                        viewModel.clearList()
                        setRandomRecommandView()
                        recommandviewVisibleController(true)
                    } else {
                        recommandviewVisibleController(false)
                    }
                    return false
                }
            }
        )
    }

    private fun setRandomRecommandView() {
        fun getRandomIndex(size: Int): Int = Random().nextInt(size)

        val suggestionItems: Array<String> = resources.getStringArray(R.array.scrap_recommand_list)
        val randomIndex = getRandomIndex(suggestionItems.size)
        val randomItem = suggestionItems[randomIndex]

        viewModel.searchImageResult(randomItem)
        binding.scrapRecommandTitle.text = randomItem + "은 어떤가요?"
        binding.scrapRecommandContainer.setOnClickListener {
            binding.scrapSearchView.setQuery(randomItem, true)
//            viewModel.searchAPIResult(randomItem, scrapContext)
            recommandviewVisibleController(false)
        }
    }

    private fun initViewModel() = with(viewModel) {
        searchList.observe(viewLifecycleOwner) {

            scrapAdapter.submitList(it)

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.scrapLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        imageResult.observe(viewLifecycleOwner) {
            binding.scrapRecommandImage.load(it[0].thumbnail)
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.scrapLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

    }

    private fun recommandviewVisibleController(isVisible: Boolean) {
        binding.scrapRecommandContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}