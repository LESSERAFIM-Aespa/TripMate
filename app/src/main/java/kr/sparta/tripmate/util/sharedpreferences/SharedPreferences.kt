package kr.sparta.tripmate.util.sharedpreferences

import android.content.Context

object SharedPreferences {
    const val USER_KEY = "TRIPMATE"

    fun saveUid(context: Context, uid: String) {
        val editor = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE).edit()
        editor.putString("uid", uid)
        editor.apply()
    }

    fun getUid(context: Context): String {
        val spf = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)
        return spf.getString("uid", "") ?: ""
    }
    fun saveProfile(context: Context, profile: String) {
        val editor = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE).edit()
        editor.putString("profile", profile)
        editor.apply()
    }

    fun getProfile(context: Context): String {
        val spf = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)
        return spf.getString("profile", "") ?: ""
    }
    fun saveNickName(context: Context, nickName: String) {
        val editor = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE).edit()
        editor.putString("nickName", nickName)
        editor.apply()
    }

    fun getNickName(context: Context): String {
        val spf = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)
        return spf.getString("nickName", "") ?: ""
    }

}