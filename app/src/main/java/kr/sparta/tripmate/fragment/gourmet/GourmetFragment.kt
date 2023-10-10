package kr.sparta.tripmate.fragment.gourmet

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
import kr.sparta.tripmate.R
import kr.sparta.tripmate.api.NaverNetWorkClient
import kr.sparta.tripmate.api.NaverNetWorkClient.apiService
import kr.sparta.tripmate.databinding.FragmentGourmetBinding
import kr.sparta.tripmate.`interface`.ItemClick
import kr.sparta.tripmate.viewmodel.gourmet.GourmetFactory
import kr.sparta.tripmate.viewmodel.gourmet.GourmetViewModel

class GourmetFragment : Fragment() {
    private val binding by lazy { FragmentGourmetBinding.inflate(layoutInflater) }
    private lateinit var gourmetAdapter: GourmetAdapter
    private val apiServiceInstance = NaverNetWorkClient.apiService
    private val gourmetViewModel : GourmetViewModel by viewModels {GourmetFactory(apiServiceInstance)}

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
        gourmetViewModel.apply {
            gourResult.observe(viewLifecycleOwner){
                gourmetAdapter.items.clear()
                gourmetAdapter.items.addAll(it)
                gourmetAdapter.notifyDataSetChanged()
                isLoading.observe(viewLifecycleOwner){isLoading ->
                    binding.gourmetLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
                }

            }
        }
    }
    private fun searchView() {
        gourmetAdapter = GourmetAdapter(requireContext())

        binding.gourmetRecyclerView.apply {
            layoutManager = GridLayoutManager(context,3)
            adapter = gourmetAdapter
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
            gourmetViewModel.GourmetServerResults(searchQuery!!)
        }
    }
}