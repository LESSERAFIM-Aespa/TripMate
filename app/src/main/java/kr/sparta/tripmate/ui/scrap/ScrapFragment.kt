package kr.sparta.tripmate.ui.scrap

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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.databinding.FragmentScrapBinding
import kr.sparta.tripmate.domain.model.ScrapModel
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
                model.isLike = !model.isLike

                if (model.isLike) {
                    saveScrapFirebase(model)
                } else deleteScrapFirebase(model)
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

    private fun saveScrapFirebase(model: ScrapModel) {
        val scrapRef = ScrapRef()
        scrapRef.get().addOnSuccessListener {
            if (it.exists()) {
                val current_Index = it.childrenCount
                val new_Index = current_Index + 1

                scrapRef.child(new_Index.toString()).setValue(model)
            } else scrapRef.child("1").setValue(model)

        }.addOnFailureListener {
            Log.d("TripMates", "Error reading data: $it")
        }
    }

    private fun deleteScrapFirebase(model: ScrapModel) {
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
        return scrap_Database.child("UserData")
            .child(SharedPreferences.getUid(requireContext()))
            .child("scrapData")
    }

}