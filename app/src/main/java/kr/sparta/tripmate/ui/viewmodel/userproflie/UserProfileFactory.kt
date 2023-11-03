package kr.sparta.tripmate.ui.viewmodel.userproflie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.user.FirebaseUserRemoteDataSource
import kr.sparta.tripmate.data.repository.user.FirebaseUserRepositoryImpl
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.UpdateUserData

class UserProfileFactory : ViewModelProvider.Factory{
    private val repository : FirebaseUserRepository by lazy {
        FirebaseUserRepositoryImpl(FirebaseUserRemoteDataSource())
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