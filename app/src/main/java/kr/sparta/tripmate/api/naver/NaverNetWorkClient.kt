package kr.sparta.tripmate.api.naver

import com.google.firebase.BuildConfig
import com.google.gson.GsonBuilder
import kr.sparta.tripmate.api.Constants
import kr.sparta.tripmate.data.datasource.remote.ImageRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.ScrapRemoteDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NaverNetWorkClient {
    val apiService : ScrapRemoteDataSource
        get() = instance.create(ScrapRemoteDataSource::class.java)
    val imageApiService : ImageRemoteDataSource
        get() = instance.create(ImageRemoteDataSource::class.java)

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val instance: Retrofit
        get() {
            val gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(Constants.naverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createOkHttpClient())
                .build()
        }
}