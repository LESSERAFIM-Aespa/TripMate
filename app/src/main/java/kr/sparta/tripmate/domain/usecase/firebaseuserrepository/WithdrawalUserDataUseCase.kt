package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class WithdrawalUserDataUseCase(private val repository: FirebaseUserRepository) {
    operator fun invoke(uid: String){
        repository.withdrawalUserData(uid)
    }
}