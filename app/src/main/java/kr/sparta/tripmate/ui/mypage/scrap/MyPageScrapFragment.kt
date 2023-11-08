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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.databinding.FragmentBookmarkBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.ui.community.CommunityDetailActivity
import kr.sparta.tripmate.ui.scrap.detail.ScrapDetailActivity
import kr.sparta.tripmate.ui.viewmodel.mypage.scrap.MyPageScrapFactory
import kr.sparta.tripmate.ui.viewmodel.mypage.scrap.MyPageScrapViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class MyPageScrapFragment : Fragment() {
    companion object {
        fun newInstance(): MyPageScrapFragment = MyPageScrapFragment()
    }

    private val bookmarkResults = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
        }
    }

    private lateinit var scrapContext: Context
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val bookmarkAdapter by lazy {
        MyPageScrapListAdapter(
            onItemClick = { model, position ->
                if (model is SearchBlogEntity) {
                    val intent = ScrapDetailActivity.newIntentForScrap(
                        scrapContext,
                        model.copy(
                            isLike = true
                        ),
                    )
                    bookmarkResults.launch(intent)
                } else if (model is CommunityEntity) {
                   model.key?.let {
                       // 조회수 증가
                       viewModel.updateBoardViews(model.copy())

                       val intent = CommunityDetailActivity.newIntentForEntity(scrapContext, model.key)
                       startActivity(intent)
                   }
                }
//                bookmarkResults.launch(ScrapDetail.newIntentForScrap(bookmarkContext, model))
//                viewModel.updateBoardDataView(model.toCommunityEntity(), position)
            }
        )
    }
    private val viewModel: MyPageScrapViewModel by viewModels() {
        MyPageScrapFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        scrapContext = context
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
            totalScraps.observe(viewLifecycleOwner) {
                bookmarkAdapter.submitList(it)
            }
        }
    }

    /**
     * 모든 스크랩된 데이터 가져오기
     * */
    fun getAllScrapedData() {
        val uid = SharedPreferences.getUid(scrapContext)
        // 모든 스크렙데이터 가져오기
        viewModel.getAllScrapedData(uid)
    }
}