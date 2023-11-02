package kr.sparta.tripmate.domain.usecase.firebaseuserrepository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

class SaveUserData(private val repository: FirebaseUserRepository) {
    operator fun invoke(
        model: UserDataEntity, context: Context, userLiveData:
        MutableLiveData<UserDataEntity?>
    ) {
        repository.saveUserData(model, context, userLiveData)
    }
}