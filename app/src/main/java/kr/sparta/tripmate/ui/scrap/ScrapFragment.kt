package kr.sparta.tripmate.ui.scrap

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.databinding.FragmentScrapBinding
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.ui.viewmodel.scrap.ScrapFactory
import kr.sparta.tripmate.ui.viewmodel.scrap.ScrapViewModel
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

    private val scrapViewModel: ScrapViewModel by viewModels { ScrapFactory() }

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

    private fun observeViewModel() = with(scrapViewModel) {
        scrapResult.observe(viewLifecycleOwner) {

            scrapAdapter.submitList(it)

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.scrapLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun searchView() {
        scrapAdapter = ScrapAdapter(
            onItemClick = { model, position ->
                val intent = ScrapDetail.newIntentForScrap(requireContext(), model)
                scrapResults.launch(intent)
            },
            onLikeClick = { model, position ->

                scrapViewModel.updateIsLike(
                    model = model.copy(
                        isLike = !model.isLike
                    ),
                    position
                )
                //위에서 model의 isLike값을 반전시키고 뷰모델에서 업데이트되기때문에 아래의 model.isLike는 값이 반전되기 이전이다.
                if (model.isLike) {
                    deleteScrapFirebase(model)
                } else saveScrapFirebase(model)
            }
        )

        binding.scrapRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = scrapAdapter
            binding.scrapRecyclerView.addOnScrollListener(
                SearchScrollListener(
                    scrapViewModel,
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
            scrapViewModel.searchAPIResult(searchQuery!!, requireContext())
        }
    }

    private fun saveScrapFirebase(model: ScrapEntity) {
        val uid = SharedPreferences.getUid(scrapContext)
        scrapViewModel.saveScrap(uid, model)
    }


    private fun deleteScrapFirebase(model: ScrapEntity) {
        val uid = SharedPreferences.getUid(scrapContext)
        scrapViewModel.removeScrap(uid, model)
    }
}