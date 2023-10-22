package kr.sparta.tripmate.ui.community.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentCommunityBinding
import kr.sparta.tripmate.ui.community.CommunityWriteActivity
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.ui.viewmodel.community.CommunityBoardViewModel
import kr.sparta.tripmate.ui.viewmodel.community.CommunityViewModel

class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private val commuViewModel: CommunityViewModel by viewModels()
    private val boardViewModel: CommunityBoardViewModel by viewModels()

    lateinit var activity: MainActivity
    lateinit var communityContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        communityContext = context
        activity = requireActivity() as MainActivity
    }

    private val commuAdapter by lazy {      //1. 클릭 이벤트 구현
        CommunityListAdapter(
            onProfileClicked ={model, position ->
                commuViewModel.updateCommuView(model.copy(),position)
            },
            onThumbnailClicked =
            { model, position ->
                (activity).moveTabFragment(R.string.main_tab_title_mypage)
            },
            onLikeClicked = { model, position ->
                commuViewModel.updateCommuIsLike(
                    model = model.copy(
                        commuIsLike = !model.commuIsLike
                    ), position,communityContext
                )
            },
            onItemLongClicked = { model, position ->
                boardViewModel.savedBoard(
                    model = model.copy(
                        boardLike = !model.boardLike
                    ), position, communityContext
                )
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

        commuFloatBtn()
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        commuViewModel.dataModelList.observe(viewLifecycleOwner) { //5. 뷰모델에서 데이터베이스에서 받아온데이터를 관찰하고 어댑터에 넣어줍니다.
            Log.d("TripMates", "커뮤데이터 :${it[0].views}")
            commuAdapter.submitList(it)
        }
        commuViewModel.isLoading.observe(viewLifecycleOwner) {//6. 뷰모델에서 로딩중인지 감지하고 해당 뷰를
            binding.communityLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun commuFloatBtn() {
        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, CommunityWriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() = with(binding) {
        communityMainRecyclerView.apply {
            adapter = commuAdapter
            communityMainRecyclerView.setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        commuViewModel.updateDataModelList(communityContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
