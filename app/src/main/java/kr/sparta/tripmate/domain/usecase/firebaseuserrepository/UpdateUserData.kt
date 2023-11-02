package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class UpdateUserData(private val repository: FirebaseUserRepository) {
    operator fun invoke(
        uid: String, userLiveData:
        MutableLiveData<UserDataEntity?>
    ) =
        repository.updateUserData(uid,userLiveData)
}