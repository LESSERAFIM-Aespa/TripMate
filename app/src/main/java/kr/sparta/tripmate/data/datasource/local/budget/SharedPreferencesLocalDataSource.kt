package kr.sparta.tripmate.data.datasource.local.budget

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesLocalDataSource(context: Context) {
    companion object {
        private const val USER_KEY = "TRIPMATE"

        private const val USER_UID = "uid"
        private const val USER_PROFILE = "profile"
        private const val USER_NICK_NAME = "nickName"
        private const val USER_UID_FROM_USER = "uidFromUser"
    }

    private val mySharedPreferences: SharedPreferences =
        context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)

    fun saveUid(uid: String) {
        val editor = mySharedPreferences.edit()
        editor.putString(USER_UID, uid)
        editor.apply()
    }

    fun getUid(): String = mySharedPreferences.getString(USER_UID, "") ?: ""

    fun saveProfile(profile: String) {
        val editor = mySharedPreferences.edit()
        editor.putString(USER_PROFILE, profile)
        editor.apply()
    }

    fun getProfile(): String = mySharedPreferences.getString(USER_PROFILE, "") ?: ""
    fun saveNickName(nickName: String) {
        val editor = mySharedPreferences.edit()
        editor.putString(USER_NICK_NAME, nickName)
        editor.apply()
    }

    fun getNickName(): String = mySharedPreferences.getString(USER_NICK_NAME, "") ?: ""

    fun saveUidFromUser(uidFromUser: String) {
        val editor = mySharedPreferences.edit()
        editor.putString(USER_UID_FROM_USER, uidFromUser)
        editor.apply()
    }

    fun getUidFromUser(): String = mySharedPreferences.getString(USER_UID_FROM_USER, "") ?: ""

    fun removeKey() {
        val editor = mySharedPreferences.edit()
        editor.remove(USER_UID)
        editor.remove(USER_PROFILE)
        editor.remove(USER_NICK_NAME)
        editor.remove(USER_UID_FROM_USER)
        editor.clear()
        editor.apply()
    }
}