package kr.sparta.tripmate.ui.viewmodel.scrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.repository.FirebaseScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.RemoveFirebaseScrapData

class ScrapDetailFactory : ViewModelProvider.Factory {
    private val repository: FirebaseScrapRepository by lazy {
        FirebaseScrapRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScrapDetailViewModel::class.java)) {
            return ScrapDetailViewModel(
                RemoveFirebaseScrapData(repository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}