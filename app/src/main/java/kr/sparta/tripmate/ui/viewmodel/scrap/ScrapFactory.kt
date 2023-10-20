package kr.sparta.tripmate.ui.viewmodel.scrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.api.naver.NaverNetWorkClient
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseScrapRepositoryImpl
import kr.sparta.tripmate.data.repository.ScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository
import kr.sparta.tripmate.domain.repository.ScrapRepository
import kr.sparta.tripmate.domain.usecase.GetSearchBlogUseCase
import kr.sparta.tripmate.domain.usecase.RemoveFirebaseScrapData
import kr.sparta.tripmate.domain.usecase.SaveFirebaseScrapData

class ScrapFactory : ViewModelProvider.Factory {
    private val repository: ScrapRepository = ScrapRepositoryImpl(
        NaverNetWorkClient.apiService
    )
    private val firebaseRepository: FirebaseScrapRepository = FirebaseScrapRepositoryImpl(
        FirebaseDBRemoteDataSource()
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScrapViewModel::class.java)) {
            return ScrapViewModel(
                GetSearchBlogUseCase(repository),
                SaveFirebaseScrapData(firebaseRepository),
                RemoveFirebaseScrapData(firebaseRepository),
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}