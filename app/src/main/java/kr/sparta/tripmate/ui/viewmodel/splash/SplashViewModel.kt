package kr.sparta.tripmate.ui.viewmodel.splash

import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase

class SplashViewModel(private val getUidUseCase: GetUidUseCase) : ViewModel() {
    fun getUid(): String = getUidUseCase()
}