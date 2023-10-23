package kr.sparta.tripmate.ui.viewmodel.community

import FirebaseCommunityRepositoryImpl
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository
import kr.sparta.tripmate.domain.usecase.GetFirebaseCommunityData
import kr.sparta.tripmate.domain.usecase.IsLikeFirebaseCommunityData
import kr.sparta.tripmate.domain.usecase.IsViewsFirebaseCommunityData
import kr.sparta.tripmate.domain.usecase.UpdateCommuBoard

class CommunityFactory : ViewModelProvider.Factory {
    private val repository: FirebaseCommunityRepository by lazy {
        FirebaseCommunityRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            return CommunityViewModel(
                GetFirebaseCommunityData(repository),
                IsLikeFirebaseCommunityData(repository),
                IsViewsFirebaseCommunityData(repository),
                UpdateCommuBoard(repository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}