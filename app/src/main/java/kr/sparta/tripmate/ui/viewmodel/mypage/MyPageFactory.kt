package kr.sparta.tripmate.ui.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseScrapRepositoryImpl
import kr.sparta.tripmate.data.repository.FirebaseUserRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository
import kr.sparta.tripmate.domain.repository.FirebaseUserRepository
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.SaveUserData
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.UpdateUserData

class MyPageFactory : ViewModelProvider.Factory {
    private val repository : FirebaseUserRepository by lazy {
        FirebaseUserRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyPageViewModel::class.java)){
            return MyPageViewModel(
                UpdateUserData(repository),
                SaveUserData(repository)
            )as T
        }
        throw IllegalArgumentException("에러")
    }
}