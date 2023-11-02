package kr.sparta.tripmate.ui.viewmodel.home.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase

class HomeBoardFactory : ViewModelProvider.Factory {
    private val repository: FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(
            FirebaseBoardRemoteDataSource()
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeBoardViewModel::class.java)) {
            return HomeBoardViewModel(
                GetAllBoardsUseCase(repository),
                UpdateBoardViewsUseCase(repository),
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}