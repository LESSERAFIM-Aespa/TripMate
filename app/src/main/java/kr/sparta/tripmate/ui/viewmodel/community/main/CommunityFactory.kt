package kr.sparta.tripmate.ui.viewmodel.community.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBookmarkRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseCommunityBoardsRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseIsLikeRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBookmarkRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseCommunityBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseIsLikeRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseIsLikeRepository
import kr.sparta.tripmate.domain.usecase.community.board.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemLikesUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemViewsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.AddBookmarkedBoardUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.GetAllBookmarkedBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.GetBookmarkedBoardUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.RemoveBookmarkedBoardUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.UpdateBookmarkedBoardUseCase
import kr.sparta.tripmate.domain.usecase.community.like.RemoveIsLikedUseCase
import kr.sparta.tripmate.domain.usecase.community.like.UpdateIsLikeUseCase

class CommunityFactory : ViewModelProvider.Factory {
    private val boardRepository: FirebaseCommunityBoardRepository by lazy {
        FirebaseCommunityBoardRepositoryImpl(FirebaseCommunityBoardsRemoteDataSource())
    }

    private val isLikeRepository: FirebaseIsLikeRepository by lazy {
        FirebaseIsLikeRepositoryImpl(FirebaseIsLikeRemoteDataSource())
    }

    private val bookmarkedRepository: FirebaseBookmarkRepository by lazy {
        FirebaseBookmarkRepositoryImpl(FirebaseBookmarkRemoteDataSource())
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            return CommunityViewModel(
                GetAllBoardsUseCase(boardRepository),
                UpdateBoardItemViewsUseCase(boardRepository),
                UpdateBoardItemLikesUseCase(boardRepository),
                UpdateBookmarkedBoardUseCase(bookmarkedRepository),
                UpdateIsLikeUseCase(isLikeRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}