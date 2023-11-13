package kr.sparta.tripmate.ui.viewmodel.mypage.scrap

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.model.search.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetAllBlogScrapsUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import kr.sparta.tripmate.util.ScrapInterface

class MyPageScrapViewModel(
    private val getAllBlogScrapsUseCase: GetAllBlogScrapsUseCase,
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val updateBoardViewsUseCase: UpdateBoardViewsUseCase,
    private val getUidUseCase: GetUidUseCase,
) : ViewModel() {
    private val _totalScraps: MutableLiveData<List<ScrapInterface?>> = MutableLiveData()
    val totalScraps get() = _totalScraps

    fun getAllScrapedData(uid: String) = viewModelScope.launch {
        val scrapBlogs = mutableListOf<ScrapInterface>()
        val scrapBoards = mutableListOf<ScrapInterface>()

        // 내가스크랩한 게시글 목록 가져오기
        getAllBoardsUseCase.invoke().collect() { boards ->
            val list = mutableListOf<CommunityEntity>()

            // 전체 게시글목록에서 내가 스크랩한 게시글 가져오기
            boards.forEachIndexed { index, communityModel ->
                val boardScraps = communityModel?.scrapUsers?.toMutableList()
                val result = boardScraps?.find { it == uid }
                if (result != null) {
                    list.add(communityModel.toEntity())
                }
            }
            scrapBoards.addAll(list as List<ScrapInterface>)

            // 내가 스크랩한 블로그 목록 가져오기
            getAllBlogScrapsUseCase(uid).collect() { blogs ->
                scrapBlogs.addAll(blogs.toEntity() as List<ScrapInterface>)
                _totalScraps.value = scrapBlogs + scrapBoards
            }
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 조회수 증가
     * */
    fun updateBoardViews(model: CommunityEntity) = updateBoardViewsUseCase(model)
    fun getUid(): String = getUidUseCase()
}
