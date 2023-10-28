package kr.sparta.tripmate.ui.viewmodel.community.main

import FirebaseCommunityRepositoryImpl
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveBoardFirebase
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommunityBaseData
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommuIsLike
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommuIsViewFromCommuRepo
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommuBoardKey

class CommunityFactory : ViewModelProvider.Factory {
    private val repository: FirebaseCommunityRepository by lazy {
        FirebaseCommunityRepositoryImpl(FirebaseDBRemoteDataSource())
    }
    private val boardRepository : FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            return CommunityViewModel(
                UpdateCommunityBaseData(repository),
                UpdateCommuIsLike(repository),
                UpdateCommuBoardKey(repository),
                GetFirebaseBoardDataFromBoardRepo(boardRepository),
                SaveBoardFirebase(boardRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}