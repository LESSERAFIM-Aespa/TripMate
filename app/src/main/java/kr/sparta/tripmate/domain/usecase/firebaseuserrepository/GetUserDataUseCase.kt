package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.user.UserData
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(private val repository: FirebaseUserRepository) {

    operator fun invoke(uid: String): Flow<UserData?> = repository.getUserData(uid)
}