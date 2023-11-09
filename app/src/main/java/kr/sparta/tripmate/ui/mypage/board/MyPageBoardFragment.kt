package kr.sparta.tripmate.ui.mypage.board

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kr.sparta.tripmate.databinding.FragmentBoardBinding
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.viewmodel.mypage.board.MyPageBoardFactory
import kr.sparta.tripmate.ui.viewmodel.mypage.board.MyPageBoardViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class MyPageBoardFragment : Fragment() {
    companion object {
        fun newInstance(): MyPageBoardFragment = MyPageBoardFragment()
    }
    private lateinit var boardContext: Context
    private var _binding: FragmentBoardBinding? = null

    private val binding get() = _binding!!
    private val viewModel: MyPageBoardViewModel by viewModels { MyPageBoardFactory() }

    private val boardAdapter by lazy {
        MyPageBoardListAdapter(
            onItemClicked = { model ->
                model.key?.let {
                    // 조회수 업데이트
                    viewModel.updateBoardView(model)

                    // 게시글로 이동
                    val intent = CommunityDetailActivity.newIntentForEntity(boardContext, model.key)
                    startActivity(intent)
                }
            },
            onLikeClicked = {model ->
                val uid = SharedPreferences.getUid(boardContext)
                // 좋아요 업데이트
                model.key?.let {
                    viewModel.updateBoardLikes(uid, it)
                }
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        boardContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        boardMainRecyclerview.apply {
            adapter = boardAdapter
            layoutManager = LinearLayoutManager(boardContext)
            setHasFixedSize(true)
        }

        val uid = SharedPreferences.getUid(boardContext)
        // 모든내 게시글 목록 가져오기
        viewModel.getAllMyBoards(uid)
    }

    private fun initViewModel() {
        with(viewModel) {
            myBoards.observe(viewLifecycleOwner) {
                boardAdapter.submitList(it)
            }
        }
    }
}