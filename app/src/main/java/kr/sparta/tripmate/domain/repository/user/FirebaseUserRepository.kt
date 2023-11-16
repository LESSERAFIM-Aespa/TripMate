package kr.sparta.tripmate.domain.repository.user

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.user.UserData
import kr.sparta.tripmate.domain.model.user.UserDataEntity

/**
 * 작성자: 서정한
 * 내용: 유저 Repository
 * */
interface FirebaseUserRepository {
    fun getUserData(uid: String): Flow<UserData?>

    fun saveUserData(model: UserDataEntity)

    fun withdrawalUserData(uid: String)

    fun logout()

    suspend fun getNickNameData(nickname: String): Boolean
}