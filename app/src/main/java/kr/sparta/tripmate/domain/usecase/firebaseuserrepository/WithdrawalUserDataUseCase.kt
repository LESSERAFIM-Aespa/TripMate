package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import android.content.Context
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class WithdrawalUserDataUseCase(private val repository: FirebaseUserRepository) {
    operator fun invoke(uid: String){
        repository.withdrawalUserData(uid)
    }
}