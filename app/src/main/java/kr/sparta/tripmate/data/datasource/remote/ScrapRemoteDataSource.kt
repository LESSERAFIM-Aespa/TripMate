package kr.sparta.tripmate.data.datasource.remote

import kr.sparta.tripmate.api.Constants
import kr.sparta.tripmate.data.model.ScrapServerData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ScrapRemoteDataSource {
    @Headers(
        "X-Naver-Client-Id: ${Constants.client_id}",
        "X-Naver-Client-Secret: ${Constants.client_secret}"
    )

    @GET("search/blog.json")
    suspend fun getScrap(
        @Query("query") query : String,
        @Query("sort") sort: String,
        @Query("display") display: Int,
    ): ScrapServerData
}