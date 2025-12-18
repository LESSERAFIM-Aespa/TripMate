package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository
import javax.inject.Inject

class WithdrawalUserDataUseCase @Inject constructor(private val repository: FirebaseUserRepository) {
    operator fun invoke(uid: String){
        repository.withdrawalUserData(uid)
    }
}