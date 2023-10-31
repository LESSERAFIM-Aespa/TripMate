package kr.sparta.tripmate.data.datasource.remote

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.data.model.login.UserData
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.model.login.toEntity
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

/**
 * 작성자: 서정한
 * 내용: Firebase RDB의 User데이터 관리
 * */
class FirebaseUserRemoteDataSource {
    private val fireDatabase = Firebase.database
    /**
     * 작성자 : 박성수
     * 내용 : 원래 유저데이터랑 저장하는부분이랑 합치려고했는데,
     * Login에서 쓸 꺼같애서 구분해놨습니다.
     * RDB에서 USER데이터만 들고옵니다.
     */
    fun updateUserData(
        uid: String,
        userLiveData: MutableLiveData<UserDataEntity?>
    ) {
        val userRef = fireDatabase.getReference("UserData").child(uid)
        userRef.get().addOnSuccessListener {
            val getUser = it.getValue(UserData::class.java)
            userLiveData.postValue(getUser?.toEntity())
        }
    }

    /**
     * 작성자 : 박성수
     * 내용 : 자기소개를 수정하고 저장 후 업데이트 합니다.
     * 추후 프로필 사진을 수정하거나 다른 데이터를 수정해도 재사용 가능합니다.
     */
    fun saveUserData(
        model: UserDataEntity, context: Context, userLiveData:
        MutableLiveData<UserDataEntity?>
    ) {
        val userRef = fireDatabase.getReference("UserData").child(model.login_Uid!!)
        userLiveData.postValue(model)
        userRef.setValue(model)
    }

    fun resignUserData(context: Context) {
        val userRef = fireDatabase.getReference("UserData").child(SharedPreferences.getUid(context))
        userRef.removeValue().addOnSuccessListener {
            context.shortToast("정상적으로 탈퇴 되었습니다.")
        }
    }

    private fun bookMarkToast(context: Context, selectedBoardKey: Boolean) {
        if (selectedBoardKey) {
            context.shortToast("북마크에 추가 되었습니다.")
        } else context.shortToast("이미 북마크된 목록 입니다.")
    }
}