package kr.sparta.tripmate.ui.viewmodel.community.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.FirebaseStorageRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.FirebaseStorageRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.FirebaseStorageRepository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetCommunityKeyUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateCommunityWriteDataUseCase
import kr.sparta.tripmate.domain.usecase.firebasestorage.UploadImageForFirebaseStorage

class CommunityWriteFactory : ViewModelProvider.Factory {
    // RDB 레퍼지토리
    private val databaseRepository: FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    // Storage 레퍼지토리
    private val storageRepository: FirebaseStorageRepository by lazy {
        FirebaseStorageRepositoryImpl(FirebaseStorageRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityWriteViewModel::class.java)) {
            return CommunityWriteViewModel(
                UpdateCommunityWriteDataUseCase(databaseRepository),
                UploadImageForFirebaseStorage(storageRepository),
                GetCommunityKeyUseCase(databaseRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}