package kr.sparta.tripmate.util.network

import kr.sparta.tripmate.api.Constants
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader(name = "X-Naver-Client-Id:", value = Constants.client_id)
            .addHeader(name = "X-Naver-Client-Secret:", value = Constants.client_secret)
            .build()
        return chain.proceed(newRequest)
    }
}