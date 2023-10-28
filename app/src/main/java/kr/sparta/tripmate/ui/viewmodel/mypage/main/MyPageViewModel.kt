package kr.sparta.tripmate.ui.viewmodel.mypage.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.usecase.user.SaveUserDataUseCase
import kr.sparta.tripmate.domain.usecase.user.UpdateUserDataUseCase

class MyPageViewModel(
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase
) :
    ViewModel() {
    private val _userData: MutableLiveData<UserDataEntity?> = MutableLiveData()
    val userData get() = _userData

    fun updateUserData(uid: String) {
        updateUserDataUseCase(uid, _userData)
    }

    fun saveUserData(context: Context, model: UserDataEntity) {
        saveUserDataUseCase(model, context, _userData)
    }
}