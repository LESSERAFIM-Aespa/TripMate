package kr.sparta.tripmate.ui.viewmodel.community.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBookmarkRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseCommunityBoardsRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseIsLikeRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseStorageRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBookmarkRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseCommunityBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseIsLikeRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseStorageRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseIsLikeRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseStorageRepository
import kr.sparta.tripmate.domain.usecase.community.UploadImageForFirebaseStorage
import kr.sparta.tripmate.domain.usecase.community.board.AddBoardItemUseCase
import kr.sparta.tripmate.domain.usecase.community.board.GetCommunityKeyUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemUseCase

class CommunityWriteFactory : ViewModelProvider.Factory {
    private val boardRepository: FirebaseCommunityBoardRepository by lazy {
        FirebaseCommunityBoardRepositoryImpl(FirebaseCommunityBoardsRemoteDataSource())
    }

    private val storageRepository: FirebaseStorageRepository by lazy {
        FirebaseStorageRepositoryImpl(FirebaseStorageRemoteDataSource())
    }

    private val isLikeRepository: FirebaseIsLikeRepository by lazy {
        FirebaseIsLikeRepositoryImpl(FirebaseIsLikeRemoteDataSource())
    }

    private val bookmarkedBoardRepository: FirebaseBookmarkRepository by lazy {
        FirebaseBookmarkRepositoryImpl(FirebaseBookmarkRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityWriteViewModel::class.java)) {
            return CommunityWriteViewModel(
                UploadImageForFirebaseStorage(storageRepository),
                UpdateBoardItemUseCase(boardRepository),
                GetCommunityKeyUseCase(boardRepository),
                AddBoardItemUseCase(boardRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}