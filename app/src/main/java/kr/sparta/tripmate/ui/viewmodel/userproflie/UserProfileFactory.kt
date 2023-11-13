package kr.sparta.tripmate.ui.viewmodel.userproflie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.sharedpreference.SharedPreferencesLocalDataSource
import kr.sparta.tripmate.data.datasource.remote.user.FirebaseUserRemoteDataSource
import kr.sparta.tripmate.data.repository.sharedpreference.SharedPreferenceReopositoryImpl
import kr.sparta.tripmate.data.repository.user.FirebaseUserRepositoryImpl
import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetUserDataUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.SaveUidFromUserUseCase
import kr.sparta.tripmate.util.TripMateApp

class UserProfileFactory : ViewModelProvider.Factory {
    private val firebaseUserRepository: FirebaseUserRepository by lazy {
        FirebaseUserRepositoryImpl(FirebaseUserRemoteDataSource())
    }

    private val sharedPreferenceReopository: SharedPreferenceReopository by lazy {
        SharedPreferenceReopositoryImpl(SharedPreferencesLocalDataSource(TripMateApp.getApp().applicationContext))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(
                GetUserDataUseCase(firebaseUserRepository),
                SaveUidFromUserUseCase(sharedPreferenceReopository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}