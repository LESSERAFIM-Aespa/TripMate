package kr.sparta.tripmate.ui.viewmodel.setting

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.ResignUserData
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.SaveUserData
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.UpdateUserData

class SettingViewModel(
    private val updateUserData: UpdateUserData,
    private val saveUserData: SaveUserData,
    private val resignUserData: ResignUserData
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