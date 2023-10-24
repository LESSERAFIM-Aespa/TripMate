package kr.sparta.tripmate.ui.mypage.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentBoardBinding
import kr.sparta.tripmate.ui.viewmodel.mypage.BoardFactory
import kr.sparta.tripmate.ui.viewmodel.mypage.BoardViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class BoardFragment : Fragment() {
    companion object {
        fun newInstance(): BoardFragment = BoardFragment()
    }

    private lateinit var boardContext :Context
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    private val boardViewModel: BoardViewModel by viewModels { BoardFactory() }
    private val boardAdapter by lazy {
        BoardListAdapter()
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
        with(boardViewModel){
            myPage.observe(viewLifecycleOwner){
                Log.d("TripMates", "size: ${it.size}")
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
    fun updateBoard(){
        val uid = SharedPreferences.getUid(boardContext)
        boardViewModel.getBoardData(uid)
    }
}