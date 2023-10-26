package kr.sparta.tripmate.ui.viewmodel.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.SaveUserData
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.UpdateUserData

class MyPageViewModel(
    private val updateUserData: UpdateUserData,
    private val saveUserData: SaveUserData
) :
    ViewModel() {
    private val _userData: MutableLiveData<UserDataEntity?> = MutableLiveData()
    val userData get() = _userData
    fun updateUserData(uid: String) {
        updateUserData(uid, _userData)
    }
    fun saveUserData(model:UserDataEntity){
        saveUserData(model, _userData)
    }
}