package kr.sparta.tripmate.ui.viewmodel.mypage.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.SaveUserData
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.UpdateUserData

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

    fun saveUserData(context: Context, model: UserDataEntity) {
        saveUserData(model, context, _userData)
    }
}