package kr.sparta.tripmate.ui.mypage.scrap

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kr.sparta.tripmate.databinding.FragmentBookmarkBinding
import kr.sparta.tripmate.ui.scrap.ScrapDetail
import kr.sparta.tripmate.ui.viewmodel.mypage.BookmarkPageFactory
import kr.sparta.tripmate.ui.viewmodel.mypage.BookmarkPageViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class BookmarkFragment : Fragment() {
    companion object {
        fun newInstance(): BookmarkFragment = BookmarkFragment()
    }
    private val bookmarkResults = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK){}
    }

    private lateinit var bookmarkContext: Context
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val bookmarkAdapter by lazy {
        BookmarkListAdapter(
            onItemClick = { model, position ->
                bookmarkResults.launch(ScrapDetail.newIntentForScrap(bookmarkContext, model))
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

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        bookmarkRecyclerview.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = bookmarkAdapter
            setHasFixedSize(true)
        }
    }

    private fun initViewModel() {
        with(viewModel) {
            myPageList.observe(viewLifecycleOwner){
                bookmarkAdapter.submitList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 사용자가 저장한 Scrap목록을 Firebase에서 가져와 적용
        viewModel.updateScrapData(requireContext())
    }
}