package kr.sparta.tripmate.ui.viewmodel.mypage.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBookmarkRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseCommunityBoardsRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBookmarkRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseCommunityBoardRepositoryImpl
import kr.sparta.tripmate.domain.usecase.community.board.GetBoardItemUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.GetAllBookmarkedBlogsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.GetAllBookmarkedBoardsUseCase

class BookmarkPageFactory : ViewModelProvider.Factory {
    private val boardRepository by lazy {
        FirebaseCommunityBoardRepositoryImpl(FirebaseCommunityBoardsRemoteDataSource())
    }

    private val bookmarkRepository by lazy {
        FirebaseBookmarkRepositoryImpl(FirebaseBookmarkRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkPageViewModel::class.java)) {
            return BookmarkPageViewModel(
                GetBoardItemUseCase(boardRepository),
                GetAllBookmarkedBlogsUseCase(bookmarkRepository),
                GetAllBookmarkedBoardsUseCase(bookmarkRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}