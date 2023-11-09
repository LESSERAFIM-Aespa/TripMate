package kr.sparta.tripmate.ui.viewmodel.community.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.user.FirebaseUserRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseBoardScrapRepositoryImpl
import kr.sparta.tripmate.data.repository.user.FirebaseUserRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardScrapRepository
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardScrapUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetUserDataUseCase

class CommunityFactory : ViewModelProvider.Factory {
    private val repository : FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseBoardRemoteDataSource())
    }

    private val boardScrapRepository: FirebaseBoardScrapRepository by lazy {
        FirebaseBoardScrapRepositoryImpl(FirebaseBoardRemoteDataSource())
    }
    private val userDataRepository: FirebaseUserRepository by lazy{
        FirebaseUserRepositoryImpl(FirebaseUserRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            return CommunityViewModel(
                UpdateBoardLikeUseCase(repository),
                GetAllBoardsUseCase(repository),
                UpdateBoardViewsUseCase(repository),
                UpdateBoardScrapUseCase(boardScrapRepository),
                GetUserDataUseCase(userDataRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}