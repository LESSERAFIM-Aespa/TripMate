package kr.sparta.tripmate.ui.mypage.board

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.databinding.FragmentBoardBinding
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.viewmodel.mypage.board.BoardFactory
import kr.sparta.tripmate.ui.viewmodel.mypage.board.BoardViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class BoardFragment : Fragment() {
    companion object {
        fun newInstance(): BoardFragment = BoardFragment()
    }

    private lateinit var boardContext: Context
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BoardViewModel by viewModels { BoardFactory() }
    private val boardResurlts = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
        }
    }
    private val boardAdapter by lazy {
        BoardListAdapter(
            // 게시글 보기
            onItemClicked = { model, position ->
                CoroutineScope(Dispatchers.Main).launch {
                    // 조회수 증가
                    viewModel.viewMyPageBoardData(model)

                    // 페이지 이동
                    val intent = CommunityDetailActivity.newIntentForEntity(
                        boardContext,
                        model = model.copy(
                            views =(model.views?.let { Integer.parseInt(it) + 1}).toString()
                        )
                    )
                    startActivity(intent)
                }
            },
            // 좋아요 클릭
            onIsLikeClicked = { model ->
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.updateLikes(boardContext, model)
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

    private fun initViewModel() {
        with(viewModel) {
            myBoards.observe(viewLifecycleOwner) {
                boardAdapter.submitList(it)
            }
        }
    }

    private fun initView() = with(binding) {
        boardMainRecyclerview.apply {
            adapter = boardAdapter
            layoutManager = LinearLayoutManager(boardContext)
            setHasFixedSize(true)
        }
    }

    fun updateBoard() {
        CoroutineScope(Dispatchers.Main).launch {
            val uid = SharedPreferences.getUid(boardContext)
            viewModel.getAllMyBoards(uid)
        }
    }
}