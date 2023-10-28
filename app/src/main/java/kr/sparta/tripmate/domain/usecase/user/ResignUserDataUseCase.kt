package kr.sparta.tripmate.domain.usecase.user

import android.content.Context
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class ResignUserDataUseCase(private val repository: FirebaseUserRepository) {
    operator fun invoke(context: Context){
        repository.resignUserData(context)
    }
}