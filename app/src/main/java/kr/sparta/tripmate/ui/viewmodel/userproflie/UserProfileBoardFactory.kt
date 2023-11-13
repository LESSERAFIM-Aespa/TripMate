package kr.sparta.tripmate.ui.viewmodel.userproflie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.budget.SharedPreferencesLocalDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.sharedpreference.SharedPreferenceReopositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidFromUserUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import kr.sparta.tripmate.util.TripMateApp

class UserProfileBoardFactory : ViewModelProvider.Factory {
    private val firebaseBoardRepository: FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseBoardRemoteDataSource())
    }

    private val sharedPreferenceReopository: SharedPreferenceReopository by lazy {
        SharedPreferenceReopositoryImpl(SharedPreferencesLocalDataSource(TripMateApp.getApp().applicationContext))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileBoardViewModel::class.java)) {
            return UserProfileBoardViewModel(
                GetAllBoardsUseCase(firebaseBoardRepository),
                UpdateBoardViewsUseCase(firebaseBoardRepository),
                UpdateBoardLikeUseCase(firebaseBoardRepository),
                GetUidUseCase(sharedPreferenceReopository),
                GetUidFromUserUseCase(sharedPreferenceReopository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}