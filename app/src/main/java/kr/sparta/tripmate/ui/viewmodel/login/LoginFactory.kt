package kr.sparta.tripmate.ui.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.user.FirebaseUserRemoteDataSource
import kr.sparta.tripmate.data.repository.user.FirebaseUserRepositoryImpl
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.SaveUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.UpdateUserDataUseCase

class LoginFactory : ViewModelProvider.Factory {
    private val repository : FirebaseUserRepository by lazy {
        FirebaseUserRepositoryImpl(FirebaseUserRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(
                SaveUserDataUseCase(repository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}