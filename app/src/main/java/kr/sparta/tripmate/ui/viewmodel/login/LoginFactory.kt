package kr.sparta.tripmate.ui.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.sharedpreference.SharedPreferencesLocalDataSource
import kr.sparta.tripmate.data.datasource.remote.user.FirebaseUserRemoteDataSource
import kr.sparta.tripmate.data.repository.sharedpreference.SharedPreferenceReopositoryImpl
import kr.sparta.tripmate.data.repository.user.FirebaseUserRepositoryImpl
import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetNickNameDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.SaveUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.WithdrawalUserDataUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetNickNameUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.SaveNickNameUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.SaveProfileUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.SaveUidUseCase
import kr.sparta.tripmate.util.TripMateApp

class LoginFactory : ViewModelProvider.Factory {
    private val firebaseUserRepository: FirebaseUserRepository by lazy {
        FirebaseUserRepositoryImpl(FirebaseUserRemoteDataSource())
    }

    private val sharedPreferenceReopository: SharedPreferenceReopository by lazy {
        SharedPreferenceReopositoryImpl(SharedPreferencesLocalDataSource(TripMateApp.getApp().applicationContext))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                SaveUserDataUseCase(firebaseUserRepository),
                GetNickNameDataUseCase(firebaseUserRepository),
                SaveUidUseCase(sharedPreferenceReopository),
                SaveProfileUseCase(sharedPreferenceReopository),
                SaveNickNameUseCase(sharedPreferenceReopository),
                GetUidUseCase(sharedPreferenceReopository),
                GetNickNameUseCase(sharedPreferenceReopository),
                WithdrawalUserDataUseCase(firebaseUserRepository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}