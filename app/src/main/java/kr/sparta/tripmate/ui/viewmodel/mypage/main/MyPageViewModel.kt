package kr.sparta.tripmate.ui.viewmodel.mypage.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.model.user.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.SaveUserDataUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetNickNameUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val getUidUseCase: GetUidUseCase,
    private val getNickNameUseCase: GetNickNameUseCase,
) :
    ViewModel() {
    private val _userData: MutableLiveData<UserDataEntity?> = MutableLiveData()
    val userData get() = _userData

    fun getUserData(uid: String) = viewModelScope.launch {
        getUserDataUseCase(uid).collect() {
            _userData.value = it?.toEntity()
        }
    }

    fun saveUserData(model: UserDataEntity) = saveUserDataUseCase(model)
    fun getUid(): String = getUidUseCase()
    fun getNickName(): String = getNickNameUseCase()
}