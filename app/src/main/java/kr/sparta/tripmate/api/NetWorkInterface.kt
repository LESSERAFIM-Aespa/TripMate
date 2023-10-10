package kr.sparta.tripmate.api

import kr.sparta.tripmate.api.serverdata.naver.GourmetServerData
import kr.sparta.tripmate.util.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface NetWorkInterface {
    @Headers(
        "X-Naver-Client-Id: ${Constants.client_id}",
        "X-Naver-Client-Secret: ${Constants.client_secret}"
    )
    @GET("search/local.json")
    fun getGourmet(
        @QueryMap queryMap: HashMap<String, String>
    ): Call<GourmetServerData?>?
}
