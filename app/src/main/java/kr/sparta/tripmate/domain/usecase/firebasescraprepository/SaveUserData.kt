package kr.sparta.tripmate.domain.usecase.firebasescraprepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

class SaveUserData(private val repository: FirebaseScrapRepository) {
    operator fun invoke(model: UserDataEntity, userLiveData: MutableLiveData<UserDataEntity?>) {
        repository.saveUserData(model, userLiveData)
    }
}