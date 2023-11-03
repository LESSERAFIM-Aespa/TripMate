package kr.sparta.tripmate.domain.repository.user

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.user.UserDataEntity

/**
 * 작성자: 서정한
 * 내용: 유저 Repository
 * */
interface FirebaseUserRepository {
    fun updateUserData(
        uid: String, userLiveData: MutableLiveData<UserDataEntity?>
    )

    fun saveUserData(
        model: UserDataEntity, context: Context, userLiveData: MutableLiveData<UserDataEntity?>
    )

    fun resignUserData(
        context: Context
    )
}