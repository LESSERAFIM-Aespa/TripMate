package kr.sparta.tripmate.ui.viewmodel.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.usecase.community.board.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemViewsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.GetAllBookmarkedBlogsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.GetAllBookmarkedBoardsUseCase
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class HomeViewModel(
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val getAllBookmarkedBlogsUseCase: GetAllBookmarkedBlogsUseCase,
    private val updateBoardItemViewsUseCase: UpdateBoardItemViewsUseCase,

    ) : ViewModel() {
    // 홈 블로그
    private val _scraps: MutableLiveData<List<SearchBlogEntity?>> = MutableLiveData()
    val scraps get() = _scraps

    // 홈 인기글
    private val _boards: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val boards get() = _boards

    /**
     * 작성자: 서정한
     * 내용: 모든 스크랩된 목록을 가져옵니다.
     * */
    suspend fun getAllScraps(context: Context) = viewModelScope.launch {
        val uid = SharedPreferences.getUid(context)

        _scraps.value = getAllBookmarkedBlogsUseCase.invoke(uid)
    }

    /**
     * 작성자: 서정한
     * 내용: 모든 게시글을 가져와 좋아요수의 내림차순으로 정렬합니다.
     * */
    suspend fun getAllBoardsOrderByLikes() = viewModelScope.launch {
        val list = getAllBoardsUseCase.invoke()
        list.sortedByDescending { it.likes?.toInt() }

        _boards.value = list
    }

    /**
     * 작성자: 서정한
     * 내용: 게시글의 조회수 증가
     * */
    suspend fun updatesBoardViews(model: CommunityEntity) {
        viewModelScope.launch {
            // 조회수 증가
            updateBoardItemViewsUseCase.invoke(model)

            val list = getAllBoardsUseCase.invoke()
            _boards.value = list
        }
    }
}