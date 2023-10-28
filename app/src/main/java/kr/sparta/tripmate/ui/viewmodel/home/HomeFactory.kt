package kr.sparta.tripmate.ui.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBookmarkRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseCommunityBoardsRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBookmarkRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseCommunityBoardRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository
import kr.sparta.tripmate.domain.usecase.community.board.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemViewsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.GetAllBookmarkedBlogsUseCase

class HomeFactory : ViewModelProvider.Factory {
    private val boardRepository: FirebaseCommunityBoardRepository by lazy {
        FirebaseCommunityBoardRepositoryImpl(FirebaseCommunityBoardsRemoteDataSource())
    }

    private val bookmarkRepository : FirebaseBookmarkRepository by lazy {
        FirebaseBookmarkRepositoryImpl(FirebaseBookmarkRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                GetAllBoardsUseCase(boardRepository),
                GetAllBookmarkedBlogsUseCase(bookmarkRepository),
                UpdateBoardItemViewsUseCase(boardRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}