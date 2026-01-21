package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val repository : FirebaseUserRepository) {
    operator fun invoke() = repository.logout()
}