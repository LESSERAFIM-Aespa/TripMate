package kr.sparta.tripmate.ui.mypage.scrap

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kr.sparta.tripmate.databinding.FragmentBookmarkBinding
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.scrap.ScrapDetail
import kr.sparta.tripmate.ui.viewmodel.mypage.BookmarkPageFactory
import kr.sparta.tripmate.ui.viewmodel.mypage.BookmarkPageViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class BookmarkFragment : Fragment() {
    companion object {
        fun newInstance(): BookmarkFragment = BookmarkFragment()
    }

    private val bookmarkResults = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
        }
    }

    private lateinit var bookmarkContext: Context
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val bookmarkAdapter by lazy {
        BookmarkListAdapter(
            onItemClick = { model, position ->
                if (model is ScrapEntity) {
                    model.isLike = true
                    bookmarkResults.launch(ScrapDetail.newIntentForScrap(bookmarkContext, model))
                } else if (model is CommunityModelEntity) {
                    bookmarkResults.launch(
                        CommunityDetailActivity.newIntentForEntity
                            (bookmarkContext, model)
                    )
                }
//                bookmarkResults.launch(ScrapDetail.newIntentForScrap(bookmarkContext, model))
//                viewModel.updateBoardDataView(model.toCommunityEntity(), position)
            }
        )
    }
    private val viewModel: BookmarkPageViewModel by viewModels() {
        BookmarkPageFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bookmarkContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateScrap()
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        bookmarkRecyclerview.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = bookmarkAdapter
            setHasFixedSize(true)
        }
    }

    private fun initViewModel() {
        with(viewModel) {
            totalMyPage.observe(viewLifecycleOwner) {
                bookmarkAdapter.submitList(it)
                Log.d("TripMates", "List:${it}")
            }
            myPageList.observe(viewLifecycleOwner) {
                Log.d("TripMates", "List:${it}")
                mergeScrapAndBoardData()
            }
            mypageBoard.observe(viewLifecycleOwner) {
                Log.d("TripMates", "board: ${it}")
                mergeScrapAndBoardData()
            }
        }
    }

    fun updateScrap() {
        Log.d("TripMates", "호출되나?")
        val uid = SharedPreferences.getUid(bookmarkContext)
        viewModel.updateScrapData(bookmarkContext)
        viewModel.updateBoardData(uid)
        viewModel.getBoardKeyData(uid)
    }
}