package kr.sparta.tripmate.ui.userprofile.board

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
import kr.sparta.tripmate.databinding.FragmentUserProfileBoardBinding
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.viewmodel.userproflie.UserProfileBoardFactory
import kr.sparta.tripmate.ui.viewmodel.userproflie.UserProfileBoardViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

/**
 * 작성자: 서정한
 * 내용: 해당User가 작성한 글목록을 보여주는 Fragment
 * */
class UserProfileBoardFragment : Fragment() {
    companion object {
        fun newInstance(): UserProfileBoardFragment = UserProfileBoardFragment()
    }

    private lateinit var uid: String
    private val userProfileBoardViewModel: UserProfileBoardViewModel by viewModels {
        UserProfileBoardFactory()
    }
    private lateinit var boardContext: Context
    private var _binding: FragmentUserProfileBoardBinding? = null
    private val binding get() = _binding!!

    private val boardAdapter by lazy {
        UserProfileBoardListAdapter(
            onItemClicked = { model, position ->
                userProfileBoardViewModel.updateView(uid, model)
                val intent = CommunityDetailActivity.newIntentForEntity(boardContext, model)
                intent.putExtra("Data", model)
                startActivity(intent)

            },
            onLikeClicked = { model, position ->
                userProfileBoardViewModel.updateCommuIsLike(
                    model = model.copy(
                        commuIsLike = !model.commuIsLike
                    ), boardContext, uid
                )
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        boardContext = context
        uid = SharedPreferences.getUidFromUser(boardContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callDataSource()
        initView()
        initViewModel()

    }

    private fun initViewModel() {
        with(userProfileBoardViewModel) {
            userPage.observe(viewLifecycleOwner) { getList ->
                Log.d("asdfasdfasdf", "변화감지 ${getList[0]!!.commuIsLike}")
                Log.d("TripMates", "변화는 감지되는거야?")
                val filteredList = getList.filter { it?.id == uid }
                boardAdapter.submitList(filteredList)
                boardAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initView() = with(binding) {
        userProfileBoardRecyclerview.apply {
            adapter = boardAdapter
            layoutManager = LinearLayoutManager(boardContext)
            setHasFixedSize(true)
        }
    }

    fun callDataSource() {
        userProfileBoardViewModel.getFirebaseBoardData(uid)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}