package kr.sparta.tripmate.util.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 토큰을 사용하지 않는 api일 경우 커스텀 헤더 제거
        if (chain.request().headers["Auth"] == "false") {
            val newRequest = chain.request().newBuilder()
                .removeHeader("Auth")
                .build()
            return chain.proceed(newRequest)
        }

//        val token = "Bearer ${repository.getString(Common.PREF_KEY_ACC_TOKEN)}"
//        Log.i("!@# Interceptor_ACC_TOKEN", "token: $token")
        val newRequest = chain.request().newBuilder()
//            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}