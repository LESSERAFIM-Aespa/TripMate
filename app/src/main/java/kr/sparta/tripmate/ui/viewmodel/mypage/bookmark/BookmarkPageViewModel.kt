package kr.sparta.tripmate.ui.viewmodel.mypage.bookmark

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.usecase.community.board.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.GetBoardItemUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.GetAllBookmarkedBlogsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.GetAllBookmarkedBoardsUseCase
import kr.sparta.tripmate.util.ScrapInterface

class BookmarkPageViewModel(
    private val getBoardItemUseCase: GetBoardItemUseCase,
    private val getAllBookmarkBlogsUseCase: GetAllBookmarkedBlogsUseCase,
    private val getAllBookmarkedBoardsUseCase: GetAllBookmarkedBoardsUseCase,
) : ViewModel() {

    private val _blogs: MutableLiveData<List<SearchBlogEntity?>> = MutableLiveData()
    val blogs get() = _blogs

    private val _boards: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val boards get() = _boards


    val _bookmarks: MutableLiveData<List<ScrapInterface?>> = MutableLiveData()
    val bookmarks get() = _bookmarks

    /**
     * 작성자: 서정한
     * 내용: 북마크된 모든 블로그글과 게시글을 가져옴.
     * */
    suspend fun getAllBookmarkedData(uid: String) = viewModelScope.launch {
        // 북마크된 블로그들
        val blogs = getAllBookmarkBlogsUseCase.invoke(uid) as List<ScrapInterface>
        // 내가 북마크한 게시글에서 key만 추출
        val boardKeys = getAllBookmarkedBoardsUseCase.invoke(uid).map {
            it.key.toString()
        }

        // 북마크된 게시글을 전체보드에서 Sort
        val bookmarkedBoards = ArrayList<CommunityEntity>()
        for(i in boardKeys.indices) {
            getBoardItemUseCase.invoke(boardKeys[i])?.let { bookmarkedBoards.add(it) }
        }

        _bookmarks.value = blogs + bookmarkedBoards.toList() as List<ScrapInterface>
    }

}