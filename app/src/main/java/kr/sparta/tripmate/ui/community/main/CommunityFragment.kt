package kr.sparta.tripmate.ui.community.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentCommunityBinding
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.community.CommunityWriteActivity
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.ui.userprofile.main.UserProfileActivity
import kr.sparta.tripmate.ui.viewmodel.community.main.CommunityFactory
import kr.sparta.tripmate.ui.viewmodel.community.main.CommunityViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class CommunityFragment : Fragment() {
    companion object {
        fun newInstance(): CommunityFragment = CommunityFragment()
    }
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private lateinit var uid : String
    private val commuViewModel: CommunityViewModel by viewModels { CommunityFactory() }

    private lateinit var activity: MainActivity
    private lateinit var communityContext: Context

    private val writeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }
        }

    private val detailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communityContext = context
        activity = requireActivity() as MainActivity
        uid = SharedPreferences.getUid(communityContext)
    }

    private val commuAdapter by lazy {      //1. 클릭 이벤트 구현
        CommunityListAdapter(
            onBoardClicked = { model, position ->
                commuViewModel.updateBoardView(uid,model.copy())
                val intent = CommunityDetailActivity.newIntentForEntity(communityContext, model)
                startActivity(intent)
            },
            onThumbnailClicked =
            { model, position ->
                if (model.id == uid) {
                    (activity).moveTabFragment(R.string.main_tab_title_mypage)
                } else {
                    val intent = UserProfileActivity.newIntentForGetUserProfile(
                        communityContext,
                        model
                    )
                    startActivity(intent)
                }
            },
            onLikeClicked = { model, position ->
                commuViewModel.updateCommuIsLike(
                    model = model.copy(
                        commuIsLike = !model.commuIsLike
                    ),communityContext
                )
                Log.d("sssss", "클릭했을때 좋아요 버튼 ${model.commuIsLike}")
            },
            onItemLongClicked = { model, position ->
                commuViewModel.saveBookMarkData(
                    model = model.copy(boardIsLike = !model.boardIsLike), uid,communityContext)

                Log.d("ssss", "클릭했을때 북마크버튼 ${model.boardIsLike}")
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FAB Click Event
        commuFloatBtn()
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        commuViewModel.communityResults.observe(viewLifecycleOwner) {
            commuAdapter.submitList(it)
            commuAdapter.notifyDataSetChanged()
        }
        commuViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.communityLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun commuFloatBtn() {
        binding.writeBtn.setOnClickListener {
            val intent = CommunityWriteActivity.newIntentForWrite(communityContext)
            writeLauncher.launch(intent)
        }
    }

    private fun initView() = with(binding) {
        communityMainRecyclerView.apply {
            adapter = commuAdapter
            communityMainRecyclerView.setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    fun updateUI() {
        CoroutineScope(Dispatchers.Main).launch {
            commuViewModel.updateDataModelList(uid)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}