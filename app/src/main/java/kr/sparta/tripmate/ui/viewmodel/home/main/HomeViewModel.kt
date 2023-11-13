package kr.sparta.tripmate.ui.viewmodel.home.main

import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetNickNameUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetProfileUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase

class HomeViewModel(
    private val getUidUseCase: GetUidUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getNickNameUseCase: GetNickNameUseCase,
) : ViewModel() {
    fun getUid(): String = getUidUseCase()
    fun getProfile(): String = getProfileUseCase()
    fun getNickName(): String = getNickNameUseCase()
}