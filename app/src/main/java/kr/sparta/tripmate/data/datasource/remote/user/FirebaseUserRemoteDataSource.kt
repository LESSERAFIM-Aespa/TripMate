package kr.sparta.tripmate.data.datasource.remote.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.sparta.tripmate.data.model.user.UserData
import kotlin.coroutines.resume

/**
 * 작성자: 서정한
 * 내용: Firebase RDB의 User데이터 관리
 * */
class FirebaseUserRemoteDataSource {
    private final val REFERENCE_BLOG_SCRAP = "BlogScrap"
    private final val REFERENCE_USER_DATA = "UserData"
    private final val REFERENCE_NICKNAME = "NickNameData"

    private fun getReference() = Firebase.database.getReference(REFERENCE_USER_DATA)
    private fun getNickReference() = Firebase.database.getReference(REFERENCE_NICKNAME)
    private fun authReference() = FirebaseAuth.getInstance()
    private fun getBlogScrapReference(uid: String) =
        Firebase.database.getReference(REFERENCE_BLOG_SCRAP).child(uid)

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
     *
     * 추가 : 닉네임을 추가로 저장합니다. (닉네임 중복검사를 통해 닉네임을 누적저장합니다.)
     */
    fun saveUserData(model: UserData) {
        val ref = model.login_Uid?.let {
            getReference().child(it)
        }

        val nickRef = getNickReference()
        model.login_NickName?.let {
            nickRef.get().addOnSuccessListener { snapshot ->
                val nickList = snapshot.children.mapNotNull {
                    it.getValue(String::class.java)
                }.toMutableList()
                nickList.add(model.login_NickName)
                nickRef.setValue(nickList)
            }
        }
        ref?.setValue(model)
    }

    /**
     * 작성자 : 서정한
     * 내용 : 회원탈퇴.
     *
     * 추가 : RDB에서 삭제 하는 부분에서 auth에서 삭제하는 부분을 추가했습니다.
     */
    fun withdrawalUserData(uid: String) {
        val user = authReference().currentUser
        val userRef = getReference().child(uid)
        val blogRef = getBlogScrapReference(uid)
        user?.delete()
        userRef.removeValue()
        blogRef.removeValue()
    }

    /**
     * 작성자 : 박성수
     * 내용 : 로그아웃을 합니다.
     */

    fun logout() = authReference().signOut()

    /**
     * 작성자 : 박성수
     * 내용 : 따로 RDB에 저장된 닉네임 데이터를 불러옵니다.
     * 불리언값을 이용해 앱을 실행하고 닉네임으 정할때 중복체크를 합니다.
     */
    suspend fun getNickNameData(nickName: String): Boolean {
        val nickRef = getNickReference()

            val result = suspendCancellableCoroutine<Boolean> {continuation ->
                nickRef.get().addOnSuccessListener { snapshot ->
                    val isExist = snapshot.children.mapNotNull {
                        it.getValue(String::class.java)
                    }.toMutableList().find { it == nickName }.isNullOrBlank()
                    continuation.resume(isExist)
                }
            }

        return result
    }
}