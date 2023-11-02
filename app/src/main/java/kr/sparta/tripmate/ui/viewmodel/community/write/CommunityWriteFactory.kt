package kr.sparta.tripmate.ui.viewmodel.community.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseStorageRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseStorageRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseStorageRepository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.AddBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetCommunityKeyUseCase
import kr.sparta.tripmate.domain.usecase.firebasestorage.UploadImageForFirebaseStorage

class CommunityWriteFactory : ViewModelProvider.Factory {
    // RDB 레퍼지토리
    private val boardRepository: FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseBoardRemoteDataSource())
    }

    // Storage 레퍼지토리
    private val storageRepository: FirebaseStorageRepository by lazy {
        FirebaseStorageRepositoryImpl(FirebaseStorageRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityWriteViewModel::class.java)) {
            return CommunityWriteViewModel(
                AddBoardUseCase(boardRepository),
                UploadImageForFirebaseStorage(storageRepository),
                GetCommunityKeyUseCase(boardRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}