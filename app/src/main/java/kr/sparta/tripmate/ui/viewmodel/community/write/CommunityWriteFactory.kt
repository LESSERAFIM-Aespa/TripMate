package kr.sparta.tripmate.ui.viewmodel.community.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.budget.SharedPreferencesLocalDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseStorageRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseStorageRepositoryImpl
import kr.sparta.tripmate.data.repository.sharedpreference.SharedPreferenceReopositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseStorageRepository
import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.AddBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetCommunityKeyUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebasestorage.UploadImageForFirebaseStorage
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetNickNameUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetProfileUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import kr.sparta.tripmate.util.TripMateApp

class CommunityWriteFactory : ViewModelProvider.Factory {
    // RDB 레퍼지토리
    private val boardRepository: FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseBoardRemoteDataSource())
    }

    // Storage 레퍼지토리
    private val storageRepository: FirebaseStorageRepository by lazy {
        FirebaseStorageRepositoryImpl(FirebaseStorageRemoteDataSource())
    }


    private val sharedPreferenceReopository: SharedPreferenceReopository by lazy {
        SharedPreferenceReopositoryImpl(SharedPreferencesLocalDataSource(TripMateApp.getApp().applicationContext))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityWriteViewModel::class.java)) {
            return CommunityWriteViewModel(
                AddBoardUseCase(boardRepository),
                UploadImageForFirebaseStorage(storageRepository),
                GetCommunityKeyUseCase(boardRepository),
                UpdateBoardUseCase(boardRepository),
                GetUidUseCase(sharedPreferenceReopository),
                GetNickNameUseCase(sharedPreferenceReopository),
                GetProfileUseCase(sharedPreferenceReopository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}