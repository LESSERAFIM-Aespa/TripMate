package kr.sparta.tripmate.ui.viewmodel.setting

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.usecase.user.ResignUserDataUseCase
import kr.sparta.tripmate.domain.usecase.user.SaveUserDataUseCase
import kr.sparta.tripmate.domain.usecase.user.UpdateUserDataUseCase

class SettingViewModel(
    private val updateUserData: UpdateUserDataUseCase,
    private val saveUserData: SaveUserDataUseCase,
    private val resignUserData: ResignUserDataUseCase
) : ViewModel() {
    private val _settingUserData: MutableLiveData<UserDataEntity?> = MutableLiveData()
    val settingUserData get() = _settingUserData

    fun updateUserData(uid: String) {
        updateUserData(uid, _settingUserData)
    }

    fun saveUserData(model: UserDataEntity, context: Context) {
        saveUserData(model, context, _settingUserData)
    }

    fun removeUserData(context: Context) {
        resignUserData(context)
    }
}