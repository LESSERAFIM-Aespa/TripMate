package kr.sparta.tripmate.ui.viewmodel.community.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.budget.SharedPreferencesLocalDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseBoardScrapRepositoryImpl
import kr.sparta.tripmate.data.repository.sharedpreference.SharedPreferenceReopositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardScrapRepository
import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.RemoveBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardScrapUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import kr.sparta.tripmate.util.TripMateApp

class CommunityDetailFactory : ViewModelProvider.Factory {
    private val firebaseBoardRepository : FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseBoardRemoteDataSource())
    }

    private val boardScrapRepository: FirebaseBoardScrapRepository by lazy {
        FirebaseBoardScrapRepositoryImpl(FirebaseBoardRemoteDataSource())
    }

    private val sharedPreferenceReopository: SharedPreferenceReopository by lazy {
        SharedPreferenceReopositoryImpl(SharedPreferencesLocalDataSource(TripMateApp.getApp().applicationContext))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityDetailViewModel::class.java)) {
            return CommunityDetailViewModel(
                UpdateBoardScrapUseCase(boardScrapRepository),
                RemoveBoardUseCase(firebaseBoardRepository),
                UpdateBoardLikeUseCase(firebaseBoardRepository),
                GetBoardUseCase(firebaseBoardRepository),
                GetUidUseCase(sharedPreferenceReopository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}