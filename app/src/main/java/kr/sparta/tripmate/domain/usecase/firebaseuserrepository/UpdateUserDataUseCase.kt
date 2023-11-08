package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class UpdateUserDataUseCase(private val repository: FirebaseUserRepository) {
    operator fun invoke(model: UserDataEntity) = repository.saveUserData(model)
}