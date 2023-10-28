package kr.sparta.tripmate.ui.viewmodel.mypage.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseCommunityBoardsRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseIsLikeRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseCommunityBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseIsLikeRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseIsLikeRepository
import kr.sparta.tripmate.domain.usecase.community.board.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.GetMyBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemLikesUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemViewsUseCase
import kr.sparta.tripmate.domain.usecase.community.like.UpdateIsLikeUseCase

class BoardFactory : ViewModelProvider.Factory {
    private val boardRepository: FirebaseCommunityBoardRepository by lazy {
        FirebaseCommunityBoardRepositoryImpl(FirebaseCommunityBoardsRemoteDataSource())
    }

    private val likeRepository: FirebaseIsLikeRepository by lazy {
        FirebaseIsLikeRepositoryImpl(FirebaseIsLikeRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardViewModel::class.java)) {
            return BoardViewModel(
                GetMyBoardsUseCase(boardRepository),
                GetAllBoardsUseCase(boardRepository),
                UpdateBoardItemViewsUseCase(boardRepository),
                UpdateBoardItemLikesUseCase(boardRepository),
                UpdateIsLikeUseCase(likeRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}