package kr.sparta.tripmate.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.repository.FirebaseUserRepository

class FirebaseUserRepositoryImpl(private val remoteSource: FirebaseDBRemoteDataSource) :
    FirebaseUserRepository {
    override fun updateUserData(uid: String, userLiveData: MutableLiveData<UserDataEntity?>) {
        remoteSource.updateUserData(uid, userLiveData)
    }

    override fun saveUserData(
        model: UserDataEntity,
        context: Context,
        userLiveData: MutableLiveData<UserDataEntity?>
    ) {
        remoteSource.saveUserData(model, context, userLiveData)
    }

    override fun resignUserData(context: Context) {
        remoteSource.resignUserData(context)
    }
}