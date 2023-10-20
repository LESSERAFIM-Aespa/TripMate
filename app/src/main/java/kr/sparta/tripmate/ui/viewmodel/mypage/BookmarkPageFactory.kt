package kr.sparta.tripmate.ui.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.GetFirebaseDatabaseRemoteSource
import kr.sparta.tripmate.data.repository.FirebaseScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository
import kr.sparta.tripmate.domain.usecase.GetFirebaseScrapData

class BookmarkPageFactory : ViewModelProvider.Factory {
    private val repository: FirebaseScrapRepository by lazy {
        FirebaseScrapRepositoryImpl(GetFirebaseDatabaseRemoteSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkPageViewModel::class.java)) {
            return BookmarkPageViewModel(GetFirebaseScrapData(repository)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}