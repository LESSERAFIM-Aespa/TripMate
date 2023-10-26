package kr.sparta.tripmate.domain.usecase.firebasescraprepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

class UpdateUserData(private val repository: FirebaseScrapRepository) {
    operator fun invoke(
        uid: String, userLiveData:
        MutableLiveData<UserDataEntity?>
    ) =
        repository.updateUserData(uid,userLiveData)
}