package kr.sparta.tripmate.ui.mypage.board

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentBoardBinding
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.scrap.ScrapDetail
import kr.sparta.tripmate.ui.viewmodel.mypage.BoardFactory
import kr.sparta.tripmate.ui.viewmodel.mypage.BoardViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class BoardFragment : Fragment() {
    companion object {
        fun newInstance(): BoardFragment = BoardFragment()
    }

    private lateinit var boardContext: Context
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    private val boardViewModel: BoardViewModel by viewModels { BoardFactory() }
    private val boardResurlts = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
        }
    }
    private val boardAdapter by lazy {
        BoardListAdapter(
            onProfileClicked = { model, position ->
                val intent = CommunityDetailActivity.newIntentForEntity(boardContext, model)
                intent.putExtra("Data", model)
                startActivity(intent)
                boardViewModel.updateBoardView(model.copy())
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
        val uid = SharedPreferences.getUid(boardContext)
        with(boardViewModel) {
            myPage.observe(viewLifecycleOwner) {
                val filteredList = it.filter { it?.id == uid }
                boardAdapter.submitList(filteredList)
                boardAdapter.notifyDataSetChanged()
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
        boardViewModel.getBoardData()
    }
}