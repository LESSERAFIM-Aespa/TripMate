package kr.sparta.tripmate.ui.viewmodel.userproflie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.usecase.user.UpdateUserDataUseCase

class UserProfileViewModel(private val updateUserDataUseCase: UpdateUserDataUseCase) : ViewModel() {
    private val _userProfileResults: MutableLiveData<UserDataEntity?> = MutableLiveData()
    val userProfileResult get() = _userProfileResults

    fun updateUserData(uid: String) {
        updateUserDataUseCase(uid, _userProfileResults)
    }
}