package kr.sparta.tripmate.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kr.sparta.tripmate.databinding.FragmentHomeBinding
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.ui.scrap.ScrapDetail
import kr.sparta.tripmate.ui.viewmodel.home.FirstViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class HomeFragment : Fragment() {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val firstViewModel: FirstViewModel by viewModels()
    private lateinit var homeFirstAdapter: HomeFirstAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inputToolBar()
        clickedProfile()
        clickedScrap()
        clickedCommu()
        clickedBudget()

        homeView()

        return binding.root
    }

    private fun homeView() {
        homeFirstAdapter = HomeFirstAdapter(
            onItemClick = { model, position ->
                val intent = ScrapDetail.newIntentForScrap(requireContext(), model)
            }
        )
        binding.homeRecyclerView1.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = homeFirstAdapter
            setHasFixedSize(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        firstViewModel.apply {
            getFirstData(SharedPreferences.getUid(requireContext())).observe(viewLifecycleOwner) {
                homeFirstAdapter.submitList(it)
            }
        }
    }


    private fun clickedBudget() {
        binding.homeArrow3.setOnClickListener {
            (context as MainActivity).onBudgetClicked()
        }
    }

    private fun clickedCommu() {
        binding.homeArrow2.setOnClickListener {
            (context as MainActivity).onCommuClicked()
        }
    }

    private fun clickedScrap() {
        binding.homeArrow1.setOnClickListener {
            (context as MainActivity).onScrapClicked()
        }
    }

    private fun clickedProfile() {
        binding.homeProfileImage.setOnClickListener {
            (context as MainActivity).onMyPageClicked()
        }
    }

    private fun inputToolBar() {
        binding.homeProfileTitle.text = "${SharedPreferences.getNickName(requireContext())} 님"
        Glide.with(requireContext())
            .load(SharedPreferences.getProfile(requireContext()))
            .into(binding.homeProfileImage)
    }
}