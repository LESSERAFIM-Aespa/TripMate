package kr.sparta.tripmate.data.repository.user

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kr.sparta.tripmate.data.datasource.remote.user.FirebaseUserRemoteDataSource
import kr.sparta.tripmate.data.model.user.UserData
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.model.user.toModel
import kr.sparta.tripmate.domain.repository.user.FirebaseUserRepository

/**
 * 작성자: 서정한
 * 내용: 유저 Repository
 * */
class FirebaseUserRepositoryImpl(private val remoteSource: FirebaseUserRemoteDataSource) :
    FirebaseUserRepository {
    override fun getUserData(uid: String): Flow<UserData?> = remoteSource.getUserData(uid)

    override fun saveUserData(model: UserDataEntity) = remoteSource.saveUserData(model.toModel())

    override fun withdrawalUserData(uid: String) = remoteSource.withdrawalUserData(uid)

    override fun logout() = remoteSource.logout()

    override suspend fun getNickNameData(nickName: String) : Boolean = remoteSource.getNickNameData(nickName)
}