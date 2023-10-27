package kr.sparta.tripmate.ui.viewmodel.userproflie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseUserRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseUserRepository
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.UpdateUserData

class UserProfileFactory : ViewModelProvider.Factory{
    private val repository : FirebaseUserRepository by lazy {
        FirebaseUserRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserProfileViewModel::class.java)){
            return UserProfileViewModel(
                UpdateUserData(repository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}