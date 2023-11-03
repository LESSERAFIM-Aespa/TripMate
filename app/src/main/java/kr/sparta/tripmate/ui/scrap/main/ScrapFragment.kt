package kr.sparta.tripmate.ui.scrap

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.databinding.FragmentScrapBinding
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.ui.scrap.detail.ScrapDetailActivity
import kr.sparta.tripmate.ui.scrap.main.ScrapAdapter
import kr.sparta.tripmate.ui.scrap.main.SearchScrollListener
import kr.sparta.tripmate.ui.viewmodel.scrap.main.SearchBlogFactory
import kr.sparta.tripmate.ui.viewmodel.scrap.main.SearchBlogViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences
import java.util.Random

class ScrapFragment : Fragment() {
    companion object {
        fun newInstance(): ScrapFragment = ScrapFragment()
    }

    var searchLoading = false
    private lateinit var scrapContext: Context
    private val binding by lazy { FragmentScrapBinding.inflate(layoutInflater) }

    private lateinit var scrapAdapter: ScrapAdapter
    private val scrapResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
            }
        }

    private val viewModel: SearchBlogViewModel by viewModels { SearchBlogFactory() }

    var searchQuery: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        scrapContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setUpView()
        searchView()
        initImage() //초기 화면 이미지 불러오는 함수입니다.
        return binding.root
    }

    private fun initImage() {
        val suggestionItems: ArrayList<String> = arrayListOf("울산 여행지", "부산 여행지","인천 여행지", "대구 여행지", "서울 여행지", "대전 여행지", "광주 여행지", "세종 여행지" )
        //추천여행지 하드코딩 한 부분입니다.
        val random = Random()
        //추천여행지 중에서 랜덤으로 하나 뽑기위한 랜덤 함수입니다.
        val randomIndex = random.nextInt(suggestionItems.size)
        //nexInt함수를 써서 0부터 suggestionItems.size -1까지의 인덱스를 변수에 담아줬습니다.
        //왜냐하면 suggestionItems.size는 항목이 8개이므로 8이되는데 실제 우리가 목록을 뽑아오기위해서는
        //리스트의 인덱스가 0부터시작하기때문에 위의 경우에는 인덱스가 0~7인 상황입니다.
        //그래서 만약 랜덤 주사위중에 suggestionItems.size인 8이 떠버리면 문제가 생기기때문에 nextInt함수를 써준겁니다.
        val randomItem = suggestionItems[randomIndex]
        //추천여행지 하드코딩한 리스트에서 인덱스번째를 랜덤으로 뽑은 항목을 randomItem에 넣어줍니다.
        viewModel.searchImageResult(randomItem)
        //예를들어 위에서 울산 여행지를 뽑아줬으면 그걸 뷰모델에 넘깁니다.
        //이미지불러오는 API를 통해 위에서 뽑은 울산 여행지를 검색어로 강제로 넣어서 이미지를 불러옵니다.(어제만든 네이버이미지API)
        binding.scrapImagesearchTextview.text = randomItem + "로 떠나볼까요?"
        //처음 표시되는 화면에 밑에 여행지+ 텍스트"로 떠나볼까요?"를 써서 표시해줍니다.
        binding.scrapImagelayout.setOnClickListener {
            //처음 화면에 표시되는 이미지와 텍스트들이 포함된 레이아웃을 클릭한다면
            viewModel.searchAPIResult(randomItem, scrapContext)
            //위에서 울산여행지를 뽑았다고 치면 울산여행지와 context를 뷰모델에 다시 넣어주는데
            // 이번에는 기존에 searchView를 이용해서 검색했을때 네이버블로그 API를 통해서 네이버블로그를 불러오는 그쪽에 검색어를 넣어준겁니다.
            //82줄 에서는 네이버이미지API고, 여긴 기존의 네이버블로그검색API입니다.
            binding.scrapRecyclerView.visibility=View.VISIBLE
            //우리가 처음화면에서 추천여행지를 클릭하고나면 블로그검색을 하기때문에 기존의 리사이클러뷰가 보여야겠죠?
            binding.scrapImagelayout.visibility= View.GONE
            //리사이클러뷰가보이면 처음에 보여주던 추천여행지 레이아웃은 없어져야겠죠?
        }
    }
    override fun onPause() {
        super.onPause()
        viewModel.resetList()
        searchQuery = null
        binding.scrapRecyclerView.visibility=View.GONE
        //다른페이지로 넘어갈때는 리사이클러뷰를 화면에서 없애줍니다.( 다른페이지갔다가 돌아오면 추천여행지를 보고싶어서)
        binding.scrapImagelayout.visibility= View.VISIBLE
        //아른페이지로 넘어갈때 미리 추천여행지 레이아웃을 보여주게 해놓습니다.(그래야 다른페이지에 갔다가 돌아오면 추천여행지가 보이니까요)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()

    }

    private fun observeViewModel() = with(viewModel) {
        searchList.observe(viewLifecycleOwner) {

            scrapAdapter.submitList(it)

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.scrapLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        imageResult.observe(viewLifecycleOwner){
            //82줄에서 네이버이미지검색으로 불러온 이미지를 관찰하고, 변경이 감지된다면 아래의 코드를 실행해줍니다.
            binding.scrapImagesearchView.load(it[0].thumbnail)
            //네이버이미지API의 최소 요청항목이 10개라서 이미지가 10개가들어옵니다.
            //하지만 우리는 한개만 있으면되니까 그중에 첫번째(인덱스0) 사진만 imageView에 넣어줍니다.
            Log.d("tripmatesss", "이미지데이터 : ${it[0].thumbnail}")
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                //이미지 검색하고하는동안 로딩중임을표시해야되는데 isLoading의 변화를 감지하고 아래의 코드를 실행해줍니다.
                //scrapViewModel의 71줄 searchImageResult() 함수에 보시면
                //처음 네이버이미지API를 이용해서 호출할때 isLoading을 true로 바꿔주는것을 볼 수 있습니다.(scrapViewModel의 73줄)
                binding.scrapLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
                //위에서 처음에 isLoading을 true로 해줬기떄문에 isLoading이 true라면 프로그래스바가 보이게(visible)로 해주고
                //scrapViewModel의 81줄에  _isLoading.value = false를 볼 수있을텐데 이미지 API요청을 끝난다음 false로바꿔줍니다.
                //그렇기때문에 false일떄 프로그레스바를 숨기는겁니다.
                //정리하면 이미지를 불러올때 프로그레스바를 보이게하고, 데이터를 받아오면 false로 바꿔줘서 프로그레스바를 숨깁니다.
            }
        }

    }

    private fun searchView() {
        scrapAdapter = ScrapAdapter(
            onItemClick = { model ->
                val intent = ScrapDetailActivity.newIntentForScrap(scrapContext, model)
                scrapResults.launch(intent)
            },
            onLikeClick = { model, position ->
                // 사용자의 검색결과에서 보이는 좋아요 업데이트
                viewModel.updateIsLike(
                    model = model.copy(
                        isLike = !model.isLike
                    ),
                    position
                )

                val uid = SharedPreferences.getUid(scrapContext)
                // 블로그 스크랩목록 업데이트
                viewModel.updateBlogScrap(uid, model)
            }
        )

        binding.scrapRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = scrapAdapter
            binding.scrapRecyclerView.addOnScrollListener(
                SearchScrollListener(
                    viewModel,
                    this@ScrapFragment, scrapContext
                )
            )
            setHasFixedSize(true)
        }
    }

    private fun setUpView() {
        binding.scrapSearchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        searchQuery = it.trim()
                        Log.d("TripMates", "검색어 : $searchQuery")
                        setupListeners()
                        searchLoading = !searchLoading
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }

    private fun setupListeners() {
        searchQuery?.let {
            viewModel.searchAPIResult(searchQuery!!, requireContext())
        }
    }
}