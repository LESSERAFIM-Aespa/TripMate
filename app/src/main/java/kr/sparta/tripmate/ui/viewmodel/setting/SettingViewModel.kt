package kr.sparta.tripmate.ui.viewmodel.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.model.user.toEntity
import kr.sparta.tripmate.domain.usecase.budgetrepository.DeleteAllBudgetsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.LogoutUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.WithdrawalUserDataUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.RemoveKeyUseCase

class SettingViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val withdrawalUserDataUseCase: WithdrawalUserDataUseCase,
    private val getUidUseCase: GetUidUseCase,
    private val removeKeyUseCase: RemoveKeyUseCase,
    private val deleteAllBudgetsUseCase: DeleteAllBudgetsUseCase,
    ) : ViewModel() {
    private val _settingUserData: MutableLiveData<UserDataEntity?> = MutableLiveData()
    val settingUserData get() = _settingUserData

    fun getUserData(uid: String) = viewModelScope.launch{
        getUserDataUseCase(uid).collect() {
            _settingUserData.value = it?.toEntity()
        }
    }

    fun deleteAllBudgets() = viewModelScope.launch { deleteAllBudgetsUseCase() }
    fun removeUserData(uid: String) = withdrawalUserDataUseCase(uid)

    fun logout() = logoutUseCase()

    fun getUid() : String = getUidUseCase()
    fun removeKey() = removeKeyUseCase()
}