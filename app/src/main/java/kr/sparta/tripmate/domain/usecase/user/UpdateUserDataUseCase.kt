package kr.sparta.tripmate.domain.usecase.user

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class UpdateUserDataUseCase(private val repository: FirebaseUserRepository) {
    operator fun invoke(
        uid: String, userLiveData:
        MutableLiveData<UserDataEntity?>
    ) =
        repository.updateUserData(uid,userLiveData)
}