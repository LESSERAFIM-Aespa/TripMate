package kr.sparta.tripmate.ui.viewmodel.community.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseBoardScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardScrapRepository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.RemoveBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardScrapUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase

class CommunityDetailFactory : ViewModelProvider.Factory {
    private val repository : FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseBoardRemoteDataSource())
    }

    private val boardScrapRepository: FirebaseBoardScrapRepository by lazy {
        FirebaseBoardScrapRepositoryImpl(FirebaseBoardRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityDetailViewModel::class.java)) {
            return CommunityDetailViewModel(
                UpdateBoardScrapUseCase(boardScrapRepository),
                RemoveBoardUseCase(repository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}