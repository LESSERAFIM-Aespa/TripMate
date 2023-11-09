package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class LogoutUseCase(private val repository : FirebaseUserRepository) {
    operator fun invoke() = repository.logout()
}