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
}