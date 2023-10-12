package kr.sparta.tripmate.ui.scrap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kr.sparta.tripmate.api.naver.NaverNetWorkClient
import kr.sparta.tripmate.databinding.FragmentScrapBinding
import kr.sparta.tripmate.ui.viewmodel.scrapmodel.ScrapFactory
import kr.sparta.tripmate.ui.viewmodel.scrapmodel.ScrapViewModel

class ScrapFragment : Fragment() {
    private val binding by lazy { FragmentScrapBinding.inflate(layoutInflater) }
    private lateinit var scrapAdapter: ScrapAdapter
    private val apiServiceInstance = NaverNetWorkClient.apiService
    private val scrapViewModel: ScrapViewModel by viewModels { ScrapFactory(apiServiceInstance) }
    private lateinit var scrapResults: ActivityResultLauncher<Intent>
    var searchQuery: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        scrapResults = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setUpView()
        searchView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        scrapViewModel.apply {
            gourResult.observe(viewLifecycleOwner) {
                scrapAdapter.items.clear()
                scrapAdapter.items.addAll(it)
                scrapAdapter.notifyDataSetChanged()
                isLoading.observe(viewLifecycleOwner) { isLoading ->
                    binding.gourmetLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun searchView() {
        scrapAdapter = ScrapAdapter(requireContext())

        binding.gourmetRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = scrapAdapter.apply {
                itemClick = object : ScrapAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        val intent = Intent(context, ScrapDetail::class.java)
                        val gson = GsonBuilder().create()
                        val data = gson.toJson(scrapAdapter.items[position])
                        intent.putExtra("scrapdata", data)
                        scrapResults.launch(intent)
                    }
                }
            }
            setHasFixedSize(true)
        }
    }

    private fun setUpView() {
        binding.gourmetSearchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        searchQuery = query.trim()
                        Log.d("TripMates", "검색어 : ${searchQuery}")
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
            scrapViewModel.GourmetServerResults(searchQuery!!)
        }
    }
}