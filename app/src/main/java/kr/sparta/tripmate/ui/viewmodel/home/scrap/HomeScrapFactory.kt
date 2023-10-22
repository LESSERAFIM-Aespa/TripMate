package kr.sparta.tripmate.ui.viewmodel.home.scrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository
import kr.sparta.tripmate.domain.usecase.GetFirebaseScrapData

class HomeScrapFactory: ViewModelProvider.Factory {
    private val repository: FirebaseScrapRepository by lazy {
        FirebaseScrapRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScrapViewModel::class.java)) {
            return HomeScrapViewModel(GetFirebaseScrapData(repository)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}