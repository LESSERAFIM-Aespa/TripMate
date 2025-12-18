package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import javax.inject.Inject

class SaveUserDataUseCase @Inject constructor(private val repository: FirebaseUserRepository) {
    operator fun invoke(model: UserDataEntity) {
        repository.saveUserData(model)
    }
}