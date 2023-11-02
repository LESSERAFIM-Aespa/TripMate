package kr.sparta.tripmate.data.repository.user

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.user.FirebaseUserRemoteDataSource
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

/**
 * 작성자: 서정한
 * 내용: 유저 Repository
 * */
class FirebaseUserRepositoryImpl(private val remoteSource: FirebaseUserRemoteDataSource) :
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