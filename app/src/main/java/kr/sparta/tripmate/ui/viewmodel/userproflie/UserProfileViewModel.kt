package kr.sparta.tripmate.ui.viewmodel.userproflie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.model.user.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.UpdateUserDataUseCase

class UserProfileViewModel(private val getUserDataUseCase: GetUserDataUseCase) : ViewModel() {
    private val _userProfileResults: MutableLiveData<UserDataEntity?> = MutableLiveData()
    val userProfileResult get() = _userProfileResults

    fun getUserData(uid: String) =viewModelScope.launch {
        getUserDataUseCase(uid).collect() {
            _userProfileResults.value = it?.toEntity()
        }
    }
}