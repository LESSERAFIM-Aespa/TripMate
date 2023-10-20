package kr.sparta.tripmate.ui.scrap

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
import androidx.fragment.app.activityViewModels
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

class ScrapFragment : Fragment() {
    companion object {
        fun newInstance(): ScrapFragment = ScrapFragment()
    }

    private lateinit var scrap_Database: DatabaseReference
    private val binding by lazy { FragmentScrapBinding.inflate(layoutInflater) }

    private lateinit var scrapAdapter: ScrapAdapter
    private val scrapResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
            }
        }

    private val scrapViewModel: ScrapViewModel by viewModels { ScrapFactory() }

    var searchQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scrap_Database = Firebase.database.reference

        setUpView()
        searchView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() = with(scrapViewModel) {
        scrapResult.observe(viewLifecycleOwner) {
            Log.d("TripMates", "어댑터에들어갈 isLike ${it[0].isLike} ${it[1].isLike}")
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
            scrapViewModel.ScrapServerResults(searchQuery!!, requireContext())
        }
    }

    private fun saveScrapFirebase(model: ScrapEntity) {
        /**
         * 작성자: 서정한
         * 내용: 스크랩데이터를 네이버에서 받아올때 html태그가 String에 섞여있음.
         * 검색한 데이터의 String값만 뽑아내기위한 메서드
         * */
        fun stripHtml(html: String): String {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                return Html.fromHtml(html).toString()
            }
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        }

        val scrapRef = ScrapRef()
        scrapRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val getScrapList = ArrayList<ScrapEntity>()

                for (items in dataSnapshot.children) {
                    val existingModel = items.getValue(ScrapEntity::class.java)
                    existingModel?.let {
                        getScrapList.add(it)
                    }
                }
                val isDuplicate = getScrapList.any { it.url == model.url }

                if (!isDuplicate) {
                    getScrapList.add(
                        model.copy(
                            title = stripHtml(model.title),
                            description = stripHtml(model.description)
                        )
                    )
                    Log.d("TripMates", "블로그 타이틀 : ${stripHtml(model.title)}")
                    scrapRef.setValue(getScrapList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TripMates", "Error reading data: $databaseError")
            }
        })
    }

    private fun deleteScrapFirebase(model: ScrapEntity) {
        val scrapRef = ScrapRef()

        scrapRef.get().addOnSuccessListener {
            for (child in it.children) {
                val item = child.getValue(ScrapModel::class.java)
                if (item != null && item.title == model.title && item.url == model.url) {
                    child.ref.removeValue()
                    break
                }
            }
        }.addOnFailureListener {
            Log.d("TripMates", "Error reading data: $it")
        }
    }

    private fun ScrapRef(): DatabaseReference {
        return scrap_Database.child("scrapData")
            .child(SharedPreferences.getUid(requireContext()))
    }

}