package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class GetNickNameDataUseCase(private val repository: FirebaseUserRepository) {
    suspend operator fun invoke(nickName: String): Boolean = repository.getNickNameData(nickName)
}