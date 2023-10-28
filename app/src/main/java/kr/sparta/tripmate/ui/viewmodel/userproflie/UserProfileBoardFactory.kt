package kr.sparta.tripmate.ui.viewmodel.userproflie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseCommunityBoardsRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseCommunityBoardRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository
import kr.sparta.tripmate.domain.usecase.community.board.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemViewsUseCase

class UserProfileBoardFactory : ViewModelProvider.Factory {
    private val repository: FirebaseCommunityBoardRepository by lazy {
        FirebaseCommunityBoardRepositoryImpl(FirebaseCommunityBoardsRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileBoardViewModel::class.java)) {
            return UserProfileBoardViewModel(
                GetAllBoardsUseCase(repository),
                UpdateBoardItemViewsUseCase(repository),
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}