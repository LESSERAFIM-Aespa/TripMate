package kr.sparta.tripmate.ui.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetFirebaseBoardDataFromScrapRepo
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetFirebaseBoardKeyDataFromScrapRepo
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetFirebaseScrapData
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.UpdateCommuIsViewFromScrapRepo

class BookmarkPageFactory : ViewModelProvider.Factory {
    private val repository: FirebaseScrapRepository by lazy {
        FirebaseScrapRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkPageViewModel::class.java)) {
            return BookmarkPageViewModel(
                GetFirebaseScrapData(repository),
                GetFirebaseBoardDataFromScrapRepo(repository),
                UpdateCommuIsViewFromScrapRepo(repository),
                GetFirebaseBoardKeyDataFromScrapRepo(repository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}