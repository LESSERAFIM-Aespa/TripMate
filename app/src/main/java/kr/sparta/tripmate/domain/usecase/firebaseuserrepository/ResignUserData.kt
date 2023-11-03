package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import android.content.Context
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class ResignUserData(private val repository: FirebaseUserRepository) {
    operator fun invoke(context: Context){
        repository.resignUserData(context)
    }
}