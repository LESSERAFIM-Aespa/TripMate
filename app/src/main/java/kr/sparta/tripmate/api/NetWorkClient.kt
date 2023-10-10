package kr.sparta.tripmate.api

object NetWorkClient {
//    val apiService : NetWorkInterface
//        get() = instance.create(NetWorkInterface::class.java)
//
//    private fun createOkHttpClient(): OkHttpClient {
//        val interceptor = HttpLoggingInterceptor()
//
//        if (BuildConfig.DEBUG)
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
//        else
//            interceptor.level = HttpLoggingInterceptor.Level.NONE
//
//        return OkHttpClient.Builder()
//            .connectTimeout(20, TimeUnit.SECONDS)
//            .readTimeout(20, TimeUnit.SECONDS)
//            .writeTimeout(20, TimeUnit.SECONDS)
//            .addNetworkInterceptor(interceptor)
//            .build()
//    }
//
//    private val instance: Retrofit
//        private get() {
//            val gson = GsonBuilder().setLenient().create()
//
//            return Retrofit.Builder()
//                .baseUrl(Constants.Url)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(createOkHttpClient())
//                .build()
//        }
}