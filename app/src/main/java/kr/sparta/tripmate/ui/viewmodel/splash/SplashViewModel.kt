package kr.sparta.tripmate.ui.viewmodel.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val getUidUseCase: GetUidUseCase) : ViewModel() {
    fun getUid(): String = getUidUseCase()
}