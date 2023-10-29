package kr.sparta.tripmate.ui.viewmodel.userproflie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.UpdateUserData

class UserProfileViewModel(private val updateUserData: UpdateUserData) : ViewModel() {
    private val _userProfileResults: MutableLiveData<UserDataEntity?> = MutableLiveData()
    val userProfileResult get() = _userProfileResults

    fun updateUserData(uid: String) {
        updateUserData(uid, _userProfileResults)
    }
}