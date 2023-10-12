package kr.sparta.tripmate.api.naver

import kr.sparta.tripmate.api.Constants
import kr.sparta.tripmate.data.model.ScrapServerData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface NetWorkInterface {
    @Headers(
        "X-Naver-Client-Id: ${Constants.client_id}",
        "X-Naver-Client-Secret: ${Constants.client_secret}"
    )
    @GET("search/blog.json")
    fun getScrap(
        @QueryMap queryMap: HashMap<String, String>
    ): Call<ScrapServerData?>?
}
