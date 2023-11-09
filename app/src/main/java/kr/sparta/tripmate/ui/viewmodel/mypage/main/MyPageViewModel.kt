package kr.sparta.tripmate.ui.viewmodel.mypage.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.model.user.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.SaveUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.UpdateUserDataUseCase

class MyPageViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase
) :
    ViewModel() {
    private val _userData: MutableLiveData<UserDataEntity?> = MutableLiveData()
    val userData get() = _userData

    fun getUserData(uid: String) = viewModelScope.launch {
        getUserDataUseCase(uid).collect(){
            _userData.value = it?.toEntity()
        }
    }

    fun saveUserData(model: UserDataEntity) = saveUserDataUseCase(model)
}