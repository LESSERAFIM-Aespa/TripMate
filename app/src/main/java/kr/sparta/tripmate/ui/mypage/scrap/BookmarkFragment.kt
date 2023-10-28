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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.databinding.FragmentBookmarkBinding
import kr.sparta.tripmate.ui.community.CommunityDetailActivity

import kr.sparta.tripmate.ui.viewmodel.mypage.bookmark.BookmarkPageFactory
import kr.sparta.tripmate.ui.viewmodel.mypage.bookmark.BookmarkPageViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class BookmarkFragment : Fragment() {
    companion object {
        fun newInstance(): BookmarkFragment = BookmarkFragment()
    }


    private lateinit var bookmarkContext: Context
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!


    private val bookmarkAdapter by lazy {
        BookmarkListAdapter(
            onItemClick = { model, position ->
//                bookmarkResults.launch(ScrapDetail.newIntentForScrap(bookmarkContext, model))
//                viewModel.updateBoardDataView(model.toCommunityEntity(), position)
            }
        )
    }

    private val viewModel: BookmarkPageViewModel by viewModels() {
        BookmarkPageFactory()
    }

    private val bookmarkResults = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
        }
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
            bookmarks.observe(viewLifecycleOwner) {
                bookmarkAdapter.submitList(it)
                Log.d("TripMates", "List:${it}")
            }
        }
    }

    fun updateBookmarks() {
        CoroutineScope(Dispatchers.Main).launch {
            val uid = SharedPreferences.getUid(bookmarkContext)
            viewModel.getAllBookmarkedData(uid)
        }
    }
}