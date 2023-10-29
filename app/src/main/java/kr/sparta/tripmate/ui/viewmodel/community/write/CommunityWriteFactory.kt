package kr.sparta.tripmate.ui.viewmodel.community.write

import FirebaseCommunityRepositoryImpl
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.FirebaseStorageRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseStorageRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository
import kr.sparta.tripmate.domain.repository.FirebaseStorageRepository
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.GetCommunityKeyUseCase
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommunityWriteData
import kr.sparta.tripmate.domain.usecase.firebasestorage.UploadImageForFirebaseStorage

class CommunityWriteFactory : ViewModelProvider.Factory {
    // RDB 레퍼지토리
    private val databaseRepository: FirebaseCommunityRepository by lazy {
        FirebaseCommunityRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    // Storage 레퍼지토리
    private val storageRepository: FirebaseStorageRepository by lazy {
        FirebaseStorageRepositoryImpl(FirebaseStorageRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityWriteViewModel::class.java)) {
            return CommunityWriteViewModel(
                UpdateCommunityWriteData(databaseRepository),
                UploadImageForFirebaseStorage(storageRepository),
                GetCommunityKeyUseCase(databaseRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}