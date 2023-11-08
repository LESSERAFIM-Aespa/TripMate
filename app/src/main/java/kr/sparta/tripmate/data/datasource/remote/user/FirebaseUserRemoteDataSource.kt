package kr.sparta.tripmate.data.datasource.remote.user

import android.content.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kr.sparta.tripmate.data.model.user.UserData
import kr.sparta.tripmate.domain.model.user.toEntity
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

/**
 * 작성자: 서정한
 * 내용: Firebase RDB의 User데이터 관리
 * */
class FirebaseUserRemoteDataSource {
    private fun getReference() = Firebase.database.getReference("UserData")
    /**
     * 작성자 : 서정한
     * 내용 : 유저정보를 가져옵니다.
     */
    fun getUserData(uid: String): Flow<UserData> {
        val ref = getReference().child(uid)
        return ref.snapshots.mapNotNull {
            it.getValue<UserData>()
        }
    }

    /**
     * 작성자 : 서정한
     * 내용 : 유저정보를 저장합니다.
     * 자기소개를 수정하고 저장 후 업데이트 합니다.
     * 추후 프로필 사진을 수정하거나 다른 데이터를 수정해도 재사용 가능합니다.
     */
    fun saveUserData(model: UserData) {
        val ref = model.login_Uid?.let { 
            getReference().child(it) 
        }
        
        ref?.setValue(model)
    }

    /**
     * 작성자 : 서정한
     * 내용 : 회원탈퇴.
     */
    fun withdrawalUserData(uid: String) {
        val userRef = getReference().child(uid)
        userRef.removeValue()
    }
}