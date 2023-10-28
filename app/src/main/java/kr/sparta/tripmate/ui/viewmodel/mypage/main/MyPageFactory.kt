package kr.sparta.tripmate.ui.viewmodel.mypage.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.user.FirebaseUserRemoteDataSource
import kr.sparta.tripmate.data.repository.user.FirebaseUserRepositoryImpl
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import kr.sparta.tripmate.domain.usecase.user.SaveUserDataUseCase
import kr.sparta.tripmate.domain.usecase.user.UpdateUserDataUseCase

class MyPageFactory : ViewModelProvider.Factory {
    private val repository : FirebaseUserRepository by lazy {
        FirebaseUserRepositoryImpl(FirebaseUserRemoteDataSource())
    }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyPageViewModel::class.java)){
            return MyPageViewModel(
                UpdateUserDataUseCase(repository),
                SaveUserDataUseCase(repository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}