package kr.sparta.tripmate.fragment.scrap

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kr.sparta.tripmate.api.NaverNetWorkClient
import kr.sparta.tripmate.databinding.FragmentScrapBinding
import kr.sparta.tripmate.viewmodel.scrapmodel.ScrapFactory
import kr.sparta.tripmate.viewmodel.scrapmodel.ScrapViewModel

class ScrapFragment : Fragment() {
    private val binding by lazy {FragmentScrapBinding.inflate(layoutInflater) }
    private lateinit var scrapAdapter: ScrapAdapter
    private val apiServiceInstance = NaverNetWorkClient.apiService
    private val scrapViewModel : ScrapViewModel by viewModels {ScrapFactory(apiServiceInstance)}

    var searchQuery:String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
            gourResult.observe(viewLifecycleOwner){
                scrapAdapter.items.clear()
                scrapAdapter.items.addAll(it)
                scrapAdapter.notifyDataSetChanged()
                isLoading.observe(viewLifecycleOwner){isLoading ->
                    binding.gourmetLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
                }

            }
        }
    }
    private fun searchView() {
        scrapAdapter = ScrapAdapter(requireContext())

        binding.gourmetRecyclerView.apply {
            layoutManager = GridLayoutManager(context,3)
            adapter = scrapAdapter
            setHasFixedSize(true)
        }
    }

    private fun setUpView() {
        binding.gourmetSearchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(!query.isNullOrEmpty()){
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
    private fun setupListeners(){
        searchQuery?.let {
            scrapViewModel.GourmetServerResults(searchQuery!!)
        }
    }
}