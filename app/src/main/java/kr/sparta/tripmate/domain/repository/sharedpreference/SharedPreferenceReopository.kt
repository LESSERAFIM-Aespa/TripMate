package kr.sparta.tripmate.domain.repository.sharedpreference

import android.content.Context
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

interface SharedPreferenceReopository {
    fun saveUid(uid: String)
    fun getUid(): String
    fun saveProfile(profile: String)
    fun getProfile(): String
    fun saveNickName(nickName: String)
    fun getNickName(): String
    fun saveUidFromUser(uidFromUser: String)
    fun getUidFromUser(): String
    fun removeKey()
}